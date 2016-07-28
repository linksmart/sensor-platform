package de.fhg.fit.biomos.sensorplatform.control;

import java.util.Iterator;
import java.util.List;

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

  private final String ledControlScriptFileName;

  private final SensorObserver sensorObserver;
  private final SensorWrapperFactory swFactory;
  private final Uploader uploader;

  private Thread sensorObserverThread;
  private Thread uploaderThread;
  private List<AbstractSensorWrapper> swList;

  private final int timeout;
  private int uptime;
  private boolean recording = false;

  @Inject
  public Controller(SensorObserver sensorObserver, TeLiProUploader uploader, SensorWrapperFactory swFactory, @Named("default.sensor.timeout") String timeout,
      @Named("default.recording.time") String uptime, @Named("led.control.script") String ledControlScriptFileName) {
    this.sensorObserver = sensorObserver;
    this.swFactory = swFactory;
    this.uploader = uploader;
    this.timeout = new Integer(timeout);
    this.uptime = new Integer(uptime);
    this.ledControlScriptFileName = ledControlScriptFileName;
  }

  public boolean isRecording() {
    return this.recording;
  }

  public void startupFromProjectBuildConfiguration() {
    if (!this.recording) {
      this.recording = true;
      initFromProjectBuild();
      startupThreads();
    } else {
      LOG.error("Data recording ongoing. No other startup allowed! Skipped!");
    }
  }

  public void startupFromWebConfiguration(int uptime, JSONArray sensorConfiguration) {
    if (!this.recording) {
      this.recording = true;
      this.uptime = uptime;
      initFromWebapplication(sensorConfiguration);
      startupThreads();
    } else {
      LOG.error("Data recording ongoing. No other startup allowed! Skipped!");
    }
  }

  private void startupThreads() {
    new Thread(this).start();
    LOG.info("start controller thread");
    this.sensorObserverThread = new Thread(this.sensorObserver);
    this.sensorObserverThread.start();
    LOG.info("start observer");
    this.uploaderThread = new Thread(this.uploader);
    this.uploaderThread.start();
    LOG.info("start uploader thread");
    LOG.info("all threads started");
  }

  @Override
  public void run() {
    sleep(this.uptime);
    shutdown();
    this.sensorObserverThread.interrupt();
    this.uploaderThread.interrupt();
    this.recording = false;
  }

  private void initFromProjectBuild() {
    LOG.info("initialise from project build configuration");
    this.swList = this.swFactory.setupFromProjectBuildConfiguration(this.uploader);
    init();
  }

  private void initFromWebapplication(JSONArray sensorConfiguration) {
    LOG.info("initialise from web application configuration");
    this.swList = this.swFactory.setupFromWebinterfaceConfinguration(sensorConfiguration, this.uploader);
    init();
  }

  private void init() {
    LOG.info("connecting to sensors");
    for (Iterator<AbstractSensorWrapper> iterator = this.swList.iterator(); iterator.hasNext();) {
      AbstractSensorWrapper sensorWrapper = iterator.next();
      boolean isConnected = sensorWrapper.connectToSensorBlocking(this.timeout);
      if (!isConnected) {
        sensorWrapper.shutdown();
        iterator.remove();
        LOG.info(sensorWrapper.toString() + " unreachable and removed");
      }
    }
    LOG.info("enable logging");
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.enableLogging();
    }

    this.sensorObserver.setTarget(this.swList);

    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.ledControlScriptFileName);
    LOG.info("initialise complete");
  }

  private void sleep(int seconds) {
    try {
      LOG.info("sleeping for " + seconds + " seconds");
      Thread.sleep(seconds * 1000);
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
    ShellscriptExecutor.setLED(LEDstate.STANDBY, this.ledControlScriptFileName);
  }

}
