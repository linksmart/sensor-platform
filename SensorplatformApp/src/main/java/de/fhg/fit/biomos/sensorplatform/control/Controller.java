package de.fhg.fit.biomos.sensorplatform.control;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

import static de.fhg.fit.biomos.sensorplatform.control.SensorWrapperFactory.BDADDRESS;
import static de.fhg.fit.biomos.sensorplatform.control.SensorWrapperFactory.SETTINGS;

/**
 * This class defines the general control flow of the sensorplatform.<br>
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class Controller implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private static final String START_SUCCESS_NEW = "A new recording period was started successfully!";
  private static final String START_SUCCESS_CONTINUE = "The recording period was started successfully for the remaining time!";
  private static final String START_ALREADY_RUNNING = "A recording period is already running!";
  private static final String START_SENSOR_NOT_AVAILABLE = "A sensor is not available: ";
  private static final String START_NO_SENSORS_IN_CONFIGURATION = "There are no sensors in the configuration!";
  private static final String START_SUCCESS_NEW_WLAN="New WLAN parameters were configured";
  private static final String WLAN_PARAMETERS_NOT_CHANGED="WLAN configuration parameters were not changed";
  private static final String START_SUCCESS_NEW_DONGLE="New mobile internet parameters were configured";
  private static final String DONGLE_PARAMETERS_NOT_CHANGED="Mobile internet configuration was not changed";

  private static final String MANUAL_HRS_UPLOAD = "All not-transmitted heart rate samples uploaded.";
  private static final String MANUAL_HRS_NO_DATA = "No samples available which are not yet transmitted.";

  private static final String RECORDING_FIRSTNAME = "firstname";
  private static final String RECORDING_LASTNAME = "lastname";
  private static final String RECORDING_END_TIME = "endtime";
  private static final String CONFIGURATION = "configuration";

  private final int timeoutNotification;
  private final int timeoutConnect;
  private final File recordingInfo;

  private final SensorWrapperFactory swFactory;
  private final HardwarePlatform hwPlatform;
  private final HeartRateSampleCollector hrsCollector;
  private final PulseOximeterSampleCollector pulseCollector;
  private final CC2650SampleCollector cc2650Collector;
  private final LuminoxSampleCollector luminoxCollector;

  private SensorOverseer sensorOverseer;

  private Thread hrsCollectorThread;
  private Thread pulseCollectorThread;
  private Thread cc2650CollectorThread;
  private Thread luminoxCollectorThread;
  private Thread controllerThread;
  private Thread sensorOverseerThread;
  private Thread hwPlatformThread;
  private List<AbstractSensorWrapper<?>> swList;

  private long uptimeMillis;
  private boolean recording = false;

  @Inject
  public Controller(SensorWrapperFactory swFactory, HardwarePlatform hwPlatform, HeartRateSampleCollector hrsCollector,
                    PulseOximeterSampleCollector pulseCollector, CC2650SampleCollector cc2650Collector, LuminoxSampleCollector luminoxCollector, @Named("timeout.sensor.connect") String timeoutConnect,
                    @Named("timeout.sensor.notification") String timeoutNotification, @Named("recording.info.filename") String recordingInfoFileName) {
    this.swFactory = swFactory;
    this.hwPlatform = hwPlatform;
    this.hrsCollector = hrsCollector;
    this.pulseCollector = pulseCollector;
    this.cc2650Collector = cc2650Collector;
    this.luminoxCollector=luminoxCollector;
    this.timeoutConnect = new Integer(timeoutConnect);
    this.timeoutNotification = new Integer(timeoutNotification);
    this.recordingInfo = new File(recordingInfoFileName);
  }

  /**
   * Expose running state for webinterface.
   *
   * @return true if the sensorplatform is recording
   */
  public boolean isRecording() {
    return this.recording;
  }

  /**
   * Used for stopping a recording period of the sensorplatform from the web application.
   */
  public void interruptController() {
    if (this.recording) {
      this.controllerThread.interrupt();
    }
  }

  /**
   * Scan for Bluetooth Low Energy devices.
   *
   * @param scanDuration
   *          scan duration in seconds
   * @return a list of discovered Bluetooth Low Energy devices.
   */
  public List<DetectedDevice> scan(int scanDuration) {
    return this.hwPlatform.getHcitool().scan(scanDuration);
  }

  /**
   * Called on program startup from main thread. Check if sensorplatform was shut down during recording. Try to resume recording if necessary.
   */
  public void initialise() {
    LOG.info("initialise controller");
    // restartBluetoothController(); // only for unbugging for pairing, no pairing supported from within this application
    this.hwPlatformThread = new Thread(this.hwPlatform, "hwPlatform");
    this.hwPlatformThread.start();
    if (!this.recordingInfo.exists()) {
      LOG.info("no recording period was interrupted");
      this.hwPlatform.setLEDstateSTANDBY();
    } else {
      LOG.info("a recording period was interrupted");
      try {
        Properties recProperties = new Properties();
        recProperties.load(new FileInputStream(this.recordingInfo));
        LOG.info("recording properties loaded");
        long diff = new Long(recProperties.getProperty(RECORDING_END_TIME)) - System.currentTimeMillis();
        long savedTime= new Long(recProperties.getProperty(RECORDING_END_TIME));
        LOG.info("Saved time: "+savedTime+"  Current time: "+System.currentTimeMillis());
        LOG.info("time difference is: " + Long.toString(diff) + " milliseconds");
        if (diff > 0) {
          LOG.info("a recording period was interrupted, which is not finished yet - resume");
          String result = startRecordingPeriod(diff, recProperties.getProperty(RECORDING_FIRSTNAME), recProperties.getProperty(RECORDING_LASTNAME),
                  new JSONArray(recProperties.getProperty(CONFIGURATION)), false);
          LOG.info(result);
        } else {
          LOG.info("a recording period was interrupted but it is finished now - delete file");
          this.recordingInfo.delete();
          this.hwPlatform.setLEDstateSTANDBY();
        }
      } catch (IOException e) {
        LOG.info("failed to load recording properties", e);
      }
    }
  }

  /**
   * Save the sensor configuration and the timestamp of the recording period END in a properties file.
   *
   * @param firstname
   *          the first name of the sensorplatform user
   * @param lastname
   *          the last name of the sensorplatform user
   * @param sensorConfiguration
   *          the sensor configuration for a recording period
   */
  private void saveSensorplatformConfiguration(String firstname, String lastname, JSONArray sensorConfiguration) {
    long timestamp = System.currentTimeMillis();
    Properties recProperties = new Properties();
    long suma=timestamp + this.uptimeMillis;
    LOG.info("This is the time to be stored in a file: "+this.uptimeMillis+" and this is the addition: "+suma);
    recProperties.put(RECORDING_END_TIME, Long.toString(timestamp + this.uptimeMillis));
    recProperties.put(RECORDING_FIRSTNAME, firstname);
    recProperties.put(RECORDING_LASTNAME, lastname);
    recProperties.put(CONFIGURATION, sensorConfiguration.toString());
    try {
      if (this.recordingInfo.exists()) {
        this.recordingInfo.delete();
      }
      FileOutputStream fos = new FileOutputStream(this.recordingInfo);
      recProperties.store(fos, null);
      fos.flush();
      fos.close();
      LOG.info("recording properties stored");
    } catch (IOException e) {
      LOG.error("failed to store recording properties", e);
    }
  }

  /**
   * Start a new recording period. Multiple checks are done before doing so.
   *
   * @param uptimeMillis
   *          recording period time in milliseconds
   * @param firstname
   *          the first name of the sensorplatform user
   * @param lastname
   *          the last name of the sensorplatform user
   * @param sensorConfiguration
   *          the sensor configuration for a recording period
   * @param isNewConfiguration
   *          a flag which defines if a recording period is new or resumed after restart
   * @return String a message for the frontend
   */
  public String startRecordingPeriod(long uptimeMillis, String firstname, String lastname, JSONArray sensorConfiguration, boolean isNewConfiguration) {
    boolean allSensorsAvailable = true;
    if (!this.recording) {
      this.recording = true;
      this.uptimeMillis = uptimeMillis;
      if (isNewConfiguration) {
        LOG.info("storing recording information to file");
        saveSensorplatformConfiguration(firstname, lastname, sensorConfiguration);
      }
      LOG.info("starting a new recording period");
      this.swList = this.swFactory.createSensorWrapper(sensorConfiguration, firstname, lastname);

      if (this.swList.size() == 0) {
        LOG.info("there are no SensorWrapper for this recording period - abort!");
        finish();
        return START_NO_SENSORS_IN_CONFIGURATION;
      }

      LOG.info("connecting to sensors");
      for (Iterator<AbstractSensorWrapper<?>> iterator = this.swList.iterator(); iterator.hasNext();) {
        AbstractSensorWrapper<?> asw = iterator.next();
        boolean isConnected = asw.getGatttool().connectBlocking(this.timeoutConnect);
        /*if (!isConnected) {
          LOG.info(asw.getSensor().toString() + " is not available");
          if (isNewConfiguration) {
            LOG.info("sensor has to be available at start - abort!");
            finish();
            return START_SENSOR_NOT_AVAILABLE + asw.getSensor().toString();
          } else {
            LOG.info("SensorOverseer will handle reconnection attempts for the remaining time");
            allSensorsAvailable = false;
          }
        }
        */

      }
      // getBatteryLevel();
      // try {
      // Thread.sleep(1000);
      // } catch (InterruptedException e) {
      // LOG.error("sleep failed", e);
      // }
      enableLogging();
      startThreads();
      if (allSensorsAvailable) {
        this.hwPlatform.setLEDstateRECORDING();
      }
      LOG.info("sensorplatform is recording");
      if (isNewConfiguration) {
        return START_SUCCESS_NEW;
      } else {
        return START_SUCCESS_CONTINUE;
      }
    } else {
      LOG.info("data recording ongoing. No other startup allowed! Skipped!");
      return START_ALREADY_RUNNING;
    }
  }


  /**
   * Start a new recording period. Multiple checks are done before doing so.
   *
   * @param wlanConfiguration
   *          the sensor configuration for a Wlan configuration
   * @param isNewParameters
   *          a flag which defines if new parameters for Wlan were introduced
   * @return String a message for the frontend
   */
  public String changeWlanParameters(JSONArray wlanConfiguration, boolean isNewParameters) {
    if (isNewParameters) {
      LOG.info("storing WLAN parameters to file");
      //saveSensorplatformConfiguration(firstname, lastname, sensorConfiguration);
    }
    LOG.info("changing WLAN parameters");
    //this.swList = this.swFactory.createSensorWrapper(wlanConfiguration);

    try {

      JSONObject sensorConfigEntry = wlanConfiguration.getJSONObject(0);
      JSONObject settings = sensorConfigEntry.getJSONObject(SETTINGS);


      //FileInputStream
      ProcessBuilder bf=new ProcessBuilder("/home/administrator/wlanConfig.sh",settings.getString("id"),settings.getString("password"));
      Process process = bf.start();
      int errCode = process.waitFor();
      LOG.info("Echo command executed, any errors?: " + errCode);
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = in.readLine()) != null) {
        LOG.info(line);
      }
      in.close();

      LOG.info("WLAN process gestartet");

    } catch (Exception e) {
      LOG.error("bad json fields - skip this WLAN configuration", e);
    }

    if(isNewParameters)
    {
      return START_SUCCESS_NEW_WLAN;
    } else {
      LOG.info("WLAN configuration parameters are not changed! Skipped!");
      return WLAN_PARAMETERS_NOT_CHANGED;
    }
  }

  /**
   * Start a new recording period. Multiple checks are done before doing so.
   *
   * @param dongleConfiguration
   *          the sensor configuration for a Wlan configuration
   * @param isNewParameters
   *          a flag which defines if new parameters for Wlan were introduced
   * @return String a message for the frontend
   */
  public String changeDongleParameters(JSONArray dongleConfiguration, boolean isNewParameters) {
    if (isNewParameters) {
      LOG.info("storing Dongles parameters to file");
      //saveSensorplatformConfiguration(firstname, lastname, sensorConfiguration);
    }
    LOG.info("changing mobile internet parameters");
    //this.swList = this.swFactory.createSensorWrapper(wlanConfiguration);

    try {

      JSONObject sensorConfigEntry = dongleConfiguration.getJSONObject(0);
      JSONObject settings = sensorConfigEntry.getJSONObject(SETTINGS);


      //FileInputStream
      ProcessBuilder bf=new ProcessBuilder("/home/administrator/SPF/sakis3gConfig.sh",settings.getString("APN"),settings.getString("PIN"));
      Process process = bf.start();
      int errCode = process.waitFor();
      LOG.info("Echo command executed, any errors?: " + errCode);
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = in.readLine()) != null) {
        LOG.info(line);
      }
      in.close();

      LOG.info("Mobile internet process gestartet");

    } catch (Exception e) {
      LOG.error("bad json fields - skip this mobile internet configuration", e);
    }

    if(isNewParameters)
    {
      return START_SUCCESS_NEW_DONGLE;
    } else {
      LOG.info("Mobile internet configuration parameters are not changed! Skipped!");
      return DONGLE_PARAMETERS_NOT_CHANGED;
    }
  }

  @SuppressWarnings("unused")
  private void getBatteryLevel() {
    LOG.info("get battery level");
    for (AbstractSensorWrapper<?> asw : this.swList) {
      if (asw.getGatttool().getInternalState() == Gatttool.State.CONNECTED) {
        asw.getBatteryLevel();
      }
    }
  }


  private ProcessBuilder getWLANConfig(String SSID, String password) {
    return new ProcessBuilder("/bin/sh", "-c","wpa_passphrase", SSID, password,">","/etc/wpa_supplicant/wpa_supplicant.conf");
  }

  private ProcessBuilder getWLANRestarted() {
    return new ProcessBuilder("/bin/sh", "-c","ifdown","wlan0","&&","ifup","wlan0");
  }

  /**
   * Enable logging (notifications) for all sensors of the recording period.
   */
  private void enableLogging() {
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.getSampleCollector().setUsed(true);
      if (asw.getGatttool().getInternalState() == Gatttool.State.CONNECTED) {
        LOG.info("enable logging for {}", asw.getSensor().getName());
        asw.enableLogging();
      }
    }
  }

  /**
   * Start a new SensorOverseer and Overseer thread, start threads of used SampleCollectors and start the Controller thread which will sleep for the time of the
   * recording period.
   */
  private void startThreads() {
    LOG.info("start observer");
    this.sensorOverseer = new SensorOverseer(this.hwPlatform, this.timeoutNotification, this.swList);
    this.sensorOverseerThread = new Thread(this.sensorOverseer, "overseer");
    this.sensorOverseerThread.start();
    LOG.info("start hrs collector thread");
    if (this.hrsCollector.isUsed()) {
      this.hrsCollectorThread = new Thread(this.hrsCollector, "hrsCollector");
      this.hrsCollectorThread.start();
    }
    if (this.pulseCollector.isUsed()) {
      this.pulseCollectorThread = new Thread(this.pulseCollector, "posCollector");
      this.pulseCollectorThread.start();
    }
    if (this.cc2650Collector.isUsed()) {
      this.cc2650CollectorThread = new Thread(this.cc2650Collector, "cc2650Collector");
      this.cc2650CollectorThread.start();
    }
    if (this.luminoxCollector.isUsed()) {
      this.luminoxCollectorThread = new Thread(this.luminoxCollector, "cc2650Collector");
      this.luminoxCollectorThread.start();
    }
    LOG.info("start controller thread");
    this.controllerThread = new Thread(this, "controller");
    this.controllerThread.start();
    LOG.info("all threads started");
  }

  /**
   * Sleep for the time of the recording period. Close everything gracefully afterwards.
   */
  @Override
  public void run() {
    try {
      LOG.info("sleeping for {} milliseconds", this.uptimeMillis);
      Thread.sleep(this.uptimeMillis);
      LOG.info("recording period finished regular");
    } catch (InterruptedException e) {
      LOG.warn("interrupt received - recording period finished");
    }
    LOG.info("stop threads");
    this.sensorOverseerThread.interrupt();
    if (this.hrsCollector.isUsed()) {
      this.hrsCollector.setUsed(false);
      this.hrsCollectorThread.interrupt();
    }
    if (this.pulseCollector.isUsed()) {
      this.pulseCollector.setUsed(false);
      this.pulseCollectorThread.interrupt();
    }
    if (this.cc2650Collector.isUsed()) {
      this.cc2650Collector.setUsed(false);
      this.cc2650CollectorThread.interrupt();
    }
    if (this.luminoxCollector.isUsed()) {
      this.luminoxCollector.setUsed(false);
      this.luminoxCollectorThread.interrupt();
    }
    finish();
  }

  /**
   * Close gatttools, delete recording file and update the LED to standby.
   */
  private void finish() {
    shutdownGatttools();
    this.recordingInfo.delete();
    LOG.info("recording properties deleted");
    this.recording = false;
    this.hwPlatform.setLEDstateSTANDBY();
  }

  /**
   * Shut down sub-processes and threads of the sensorplatform <b>gracefully</b>. This avoids zombie gatttool processes which may block sensors and other
   * possible <b>bad</b> things!
   */
  private void shutdownGatttools() {
    LOG.info("shutting down threads and processes gracefully");
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.disableLogging();
    }
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.getGatttool().disconnect();
    }
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.getGatttool().exitGatttool();
    }
  }

  /**
   * Upload all heart rate samples which are not yet transmitted to the webinterface.
   *
   * @param hrss
   *          all not yet transmitted HeartRateSamples from the database
   * @return String message for the frontend
   */
  public String manualHrsUpload(List<HeartRateSample> hrss) {
    if (isRecording()) {
      LOG.warn("A recording period is running. No manual upload allowed!");
      return START_ALREADY_RUNNING;
    }
    if (hrss.isEmpty()) {
      LOG.warn("No not-transmitted hrs data. No manual upload required!");
      return MANUAL_HRS_NO_DATA;
    } else {
      this.recording = true;
      this.hrsCollector.setUsed(true);
      LOG.info("put all heart rate samples in the upload queue");
      for (HeartRateSample hrs : hrss) {
        this.hrsCollector.addToQueue(hrs);
      }
      LOG.info("wait until queue is processed");
      this.hrsCollector.manualUpload();
      LOG.info("manual hrs upload finished");
      this.hrsCollector.setUsed(false);
      this.recording = false;
      return MANUAL_HRS_UPLOAD;
    }
  }
}

