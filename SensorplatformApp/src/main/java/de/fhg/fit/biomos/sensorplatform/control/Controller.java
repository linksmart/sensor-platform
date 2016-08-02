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

import de.fhg.fit.biomos.sensorplatform.main.ShellscriptExecutor;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorWrapper;
import de.fhg.fit.biomos.sensorplatform.util.LEDstate;
import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * This class defines the control flow of the sensorplatform.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class Controller implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private static final String RECORDING_END_TIME = "endtime";
  private static final String SETUP_TYPE = "setuptype";
  private static final String SETUP_TYPE_WEB = "web";
  private static final String SETUP_TYPE_BUILD = "build";
  private static final String CONFIGURATION = "configuration";

  private final String ledControlScriptFileName;

  private final SensorObserver sensorObserver;
  private final SensorWrapperFactory swFactory;
  private final Uploader uploader;

  private Thread sensorObserverThread;
  private Thread uploaderThread;
  private List<AbstractSensorWrapper> swList;

  private final int timeout;
  private long uptimeMillis;
  private boolean recording = false;

  private final File recordingInfo;

  @Inject
  public Controller(SensorObserver sensorObserver, TeLiProUploader uploader, SensorWrapperFactory swFactory, @Named("default.sensor.timeout") String timeout,
      @Named("default.recording.time") String uptime, @Named("led.control.script") String ledControlScriptFileName,
      @Named("recording.info.filename") String recordingInfoFileName) {
    this.sensorObserver = sensorObserver;
    this.swFactory = swFactory;
    this.uploader = uploader;
    this.timeout = new Integer(timeout);
    this.uptimeMillis = new Integer(uptime);
    this.ledControlScriptFileName = ledControlScriptFileName;
    this.recordingInfo = new File(recordingInfoFileName);
  }

  public boolean isRecording() {
    return this.recording;
  }

  public void checkLastSensorplatformState() {
    if (!this.recordingInfo.exists()) {
      LOG.info("no recording period was interrupted");
    } else {
      try {
        Properties recProperties = new Properties();
        recProperties.load(new FileInputStream(this.recordingInfo));
        LOG.info("recording properties loaded");
        long diff = new Long(recProperties.getProperty(RECORDING_END_TIME)) - System.currentTimeMillis();
        System.out.println("time difference is " + Long.toString(diff) + " milliseconds");
        if (diff > 0) {
          LOG.info("a recording period was interrupted, which is not finished yet - trying to resume");
          if (recProperties.getProperty(SETUP_TYPE).equals(SETUP_TYPE_WEB)) {
            startupFromWebConfiguration(diff, new JSONArray(recProperties.getProperty(CONFIGURATION)), false);
          } else if (recProperties.getProperty(SETUP_TYPE).equals(SETUP_TYPE_BUILD)) {
            startupFromProjectBuildConfiguration(diff, false);
          }
        } else {
          LOG.info("a recording period was interrupted but it is finished now - delete file");
          this.recordingInfo.delete();
        }
      } catch (IOException e) {
        LOG.info("failed to load recording properties", e);
      }
    }
  }

  private void storeSensorplatformState(String setuptype, JSONArray sensorConfiguration) {
    long timestamp = System.currentTimeMillis();
    Properties recProperties = new Properties();
    recProperties.put(RECORDING_END_TIME, Long.toString(timestamp + this.uptimeMillis));
    recProperties.put(SETUP_TYPE, setuptype);
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

  public void startupFromProjectBuildConfiguration(boolean store) {
    startupFromProjectBuildConfiguration(this.uptimeMillis, store);
  }

  private void startupFromProjectBuildConfiguration(long uptimeMillis, boolean store) {
    if (!this.recording) {
      this.recording = true;
      this.uptimeMillis = uptimeMillis;
      if (store) {
        storeSensorplatformState(SETUP_TYPE_BUILD, null);
      }
      initFromProjectBuild();
      startupThreads();
    } else {
      LOG.error("Data recording ongoing. No other startup allowed! Skipped!");
    }
  }

  public void startupFromWebConfiguration(long uptimeMillis, JSONArray sensorConfiguration, boolean store) {
    if (!this.recording) {
      this.recording = true;
      this.uptimeMillis = uptimeMillis;
      if (store) {
        storeSensorplatformState(SETUP_TYPE_WEB, sensorConfiguration);
      }
      initFromWebapplication(sensorConfiguration);
      startupThreads();
    } else {
      LOG.error("Data recording ongoing. No other startup allowed! Skipped!");
    }
  }

  private void startupThreads() {
    if (this.swList.isEmpty()) {
      LOG.info("there are no sensors connected - skip recording period!");
      ShellscriptExecutor.setLED(LEDstate.STANDBY, this.ledControlScriptFileName);
      this.recordingInfo.delete();
      this.recording = false;
    } else {
      LOG.info("start controller thread");
      new Thread(this).start();
      LOG.info("start observer");
      this.sensorObserverThread = new Thread(this.sensorObserver);
      this.sensorObserverThread.start();
      LOG.info("start uploader thread");
      this.uploaderThread = new Thread(this.uploader);
      this.uploaderThread.start();
      LOG.info("all threads started");
    }
  }

  @Override
  public void run() {
    sleep(this.uptimeMillis);
    shutdown();
    ShellscriptExecutor.setLED(LEDstate.STANDBY, this.ledControlScriptFileName);
    this.sensorObserverThread.interrupt();
    this.uploaderThread.interrupt();
    this.recordingInfo.delete();
    this.recording = false;
  }

  private void initFromProjectBuild() {
    LOG.info("initialise from project build configuration");
    this.swList = this.swFactory.setupFromProjectBuildConfiguration(this.uploader);
    init();
    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.ledControlScriptFileName);
  }

  private void initFromWebapplication(JSONArray sensorConfiguration) {
    LOG.info("initialise from web application configuration");
    this.swList = this.swFactory.setupFromWebinterfaceConfinguration(sensorConfiguration, this.uploader);
    init();
    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.ledControlScriptFileName);
  }

  private void init() {
    LOG.info("connecting to sensors");
    for (Iterator<AbstractSensorWrapper> iterator = this.swList.iterator(); iterator.hasNext();) {
      AbstractSensorWrapper sensorWrapper = iterator.next();
      boolean isConnected = sensorWrapper.connectToSensorBlocking(this.timeout);
      if (!isConnected) {
        LOG.info("Cannot connect to " + sensorWrapper.toString());
        sensorWrapper.shutdown();
        iterator.remove();
      }
    }
    LOG.info("enable logging");
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.enableLogging();
    }

    this.sensorObserver.setTarget(this.swList);
    LOG.info("initialise complete");
  }

  private void sleep(long millis) {
    try {
      LOG.info("sleeping for " + millis + " milliseconds");
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Shut down sub-processes and threads of the sensorplatform <b>gracefully</b>. This avoids zombie gatttool processes which may prevent sensors from returning
   * to standby mode and other <b>bad</b> things!
   */
  private void shutdown() {
    LOG.info("shutting down threads and processes gracefully");
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.disableLogging();
    }
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.disconnectBlocking();
    }
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.shutdown();
    }
    LOG.info("Recording period finished");
  }

}
