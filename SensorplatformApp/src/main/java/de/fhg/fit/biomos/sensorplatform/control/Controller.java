package de.fhg.fit.biomos.sensorplatform.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;
import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;

/**
 * This class defines the control flow of the sensorplatform.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class Controller implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

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

  private SensorObserver sensorObserver;

  private Thread hrsCollectorThread;
  private Thread pulseCollectorThread;
  private Thread cc2650CollectorThread;
  private Thread controllerThread;
  private Thread sensorObserverThread;
  private List<AbstractSensorWrapper<?>> swList;

  private long uptimeMillis;
  private boolean recording = false;

  @Inject
  public Controller(SensorWrapperFactory swFactory, HardwarePlatform hwPlatform, HeartRateSampleCollector hrsCollector,
      PulseOximeterSampleCollector pulseCollector, CC2650SampleCollector cc2650Collector, @Named("timeout.sensor.connect") String timeoutConnect,
      @Named("timeout.sensor.notification") String timeoutNotification, @Named("recording.info.filename") String recordingInfoFileName) {
    this.swFactory = swFactory;
    this.hwPlatform = hwPlatform;
    this.hrsCollector = hrsCollector;
    this.pulseCollector = pulseCollector;
    this.cc2650Collector = cc2650Collector;
    this.timeoutConnect = new Integer(timeoutConnect);
    this.timeoutNotification = new Integer(timeoutNotification) * 2;
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

  public void unblockController() {
    Hciconfig hciconfig = new HciconfigImpl();
    hciconfig.down();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep failed");
    }
    hciconfig.up();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep failed");
    }
  }

  /**
   * Called on program startup from main thread. Check if sensorplatform was shut down during recording. Try to resume recording after next start.
   */
  public void initialise() {
    LOG.info("initialise controller");
    LOG.info("restart bluetooth device");
    unblockController();
    if (!this.recordingInfo.exists()) {
      LOG.info("no recording period was interrupted");
      this.hwPlatform.setLEDstateSTANDBY();
    } else {
      try {
        Properties recProperties = new Properties();
        recProperties.load(new FileInputStream(this.recordingInfo));
        LOG.info("recording properties loaded");
        long diff = new Long(recProperties.getProperty(RECORDING_END_TIME)) - System.currentTimeMillis();
        LOG.info("time difference is: " + Long.toString(diff) + " milliseconds");
        if (diff > 0) {
          LOG.info("a recording period was interrupted, which is not finished yet - trying to resume");
          startup(diff, new JSONArray(recProperties.getProperty(CONFIGURATION)), false);
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
   * Save the sensor configuration and the timestamp of the recording period end in a properties file.
   *
   * @param sensorConfiguration
   */
  private void saveSensorplatformConfiguration(JSONArray sensorConfiguration) {
    long timestamp = System.currentTimeMillis();
    Properties recProperties = new Properties();
    recProperties.put(RECORDING_END_TIME, Long.toString(timestamp + this.uptimeMillis));
    recProperties.put(CONFIGURATION, sensorConfiguration.toString());
    try {
      if (this.recordingInfo.exists()) {
        this.recordingInfo.delete();
      }
      recProperties.store(new FileOutputStream(this.recordingInfo), null);
      LOG.info("recording properties stored");
    } catch (IOException e) {
      LOG.error("failed to store recording properties", e);
    }
  }

  public void startup(long uptimeMillis, JSONArray sensorConfiguration, boolean saveConfiguration) {
    if (!this.recording) {
      this.recording = true;
      this.uptimeMillis = uptimeMillis;
      if (saveConfiguration) {
        saveSensorplatformConfiguration(sensorConfiguration);
      }
      LOG.info("startup called");
      this.swList = this.swFactory.createSensorWrapper(sensorConfiguration);

      int expectedSensorCount = this.swList.size();
      int presentSensorCount = connectDevices();

      if (presentSensorCount < expectedSensorCount) {
        LOG.info("abort startup!");
        finish();
        return;
      }

      enableLogging();
      startThreads();
      this.hwPlatform.setLEDstateRECORDING();
      LOG.info("sensorplatform is recording");
    } else {
      LOG.error("data recording ongoing. No other startup allowed! Skipped!");
    }
  }

  private int connectDevices() {
    LOG.info("connecting to sensors");
    for (Iterator<AbstractSensorWrapper<?>> iterator = this.swList.iterator(); iterator.hasNext();) {
      AbstractSensorWrapper<?> sensorWrapper = iterator.next();
      boolean isConnected = sensorWrapper.getGatttool().connectBlocking(this.timeoutConnect);
      if (!isConnected) {
        LOG.info("device removed from list");
        sensorWrapper.getGatttool().exitGatttool();
        iterator.remove();
      }
    }
    return this.swList.size();
  }

  private void enableLogging() {
    LOG.info("enable logging");
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.enableLogging();
    }
  }

  private void startThreads() {
    LOG.info("start observer");
    this.sensorObserver = new SensorObserver(this.timeoutNotification, this.swList);
    this.sensorObserverThread = new Thread(this.sensorObserver);
    this.sensorObserverThread.start();
    LOG.info("start hrs collector thread");
    if (this.hrsCollector.getStartFlag()) {
      this.hrsCollectorThread = new Thread(this.hrsCollector);
      this.hrsCollectorThread.start();
    }
    if (this.pulseCollector.getStartFlag()) {
      this.pulseCollectorThread = new Thread(this.pulseCollector);
      this.pulseCollectorThread.start();
    }
    if (this.cc2650Collector.getStartFlag()) {
      this.cc2650CollectorThread = new Thread(this.cc2650Collector);
      this.cc2650CollectorThread.start();
    }
    LOG.info("start controller thread");
    this.controllerThread = new Thread(this);
    this.controllerThread.start();
    LOG.info("all threads started");
  }

  @Override
  public void run() {
    try {
      LOG.info("sleeping for " + this.uptimeMillis + " milliseconds");
      Thread.sleep(this.uptimeMillis);
      LOG.info("recording period finished regular");
    } catch (InterruptedException e) {
      LOG.warn("interrupt received - recording period finished");
    }
    LOG.info("stop threads");
    this.sensorObserverThread.interrupt();
    if (this.hrsCollector.getStartFlag()) {
      this.hrsCollectorThread.interrupt();
    }
    if (this.pulseCollector.getStartFlag()) {
      this.pulseCollectorThread.interrupt();
    }
    if (this.cc2650Collector.getStartFlag()) {
      this.cc2650CollectorThread.interrupt();
    }
    finish();
  }

  private void finish() {
    shutdownGatttools();
    this.recordingInfo.delete();
    LOG.info("recording properties deleted");
    this.recording = false;
    this.hwPlatform.setLEDstateSTANDBY();
  }

  /**
   * Shut down sub-processes and threads of the sensorplatform <b>gracefully</b>. This avoids zombie gatttool processes which may block sensors and other
   * <b>bad</b> things!
   */
  private void shutdownGatttools() {
    LOG.info("shutting down threads and processes gracefully");
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.disableLogging();
    }
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.getGatttool().disconnectBlocking();
    }
    for (AbstractSensorWrapper<?> asw : this.swList) {
      asw.getGatttool().exitGatttool();
    }
  }
}
