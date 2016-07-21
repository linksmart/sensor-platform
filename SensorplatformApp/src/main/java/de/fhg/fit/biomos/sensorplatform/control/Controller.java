package de.fhg.fit.biomos.sensorplatform.control;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

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

  private final SensorWrapperFactory swFactory;

  private List<SensorWrapper> swList;

  private final String ledControlScriptFileName;

  private int timeout;
  private int uptime;
  private boolean running = false;

  @Inject
  public Controller(SensorWrapperFactory swFactory, @Named("default.sensor.timeout") String timeout, @Named("default.recording.time") String uptime,
      @Named("led.control.script") String ledControlScriptFileName) {
    this.swFactory = swFactory;
    this.timeout = new Integer(timeout);
    this.uptime = new Integer(uptime);
    this.ledControlScriptFileName = ledControlScriptFileName;
  }

  public void startupFromProjectBuildConfiguration() throws RuntimeException {
    if (!this.running) {
      initFromProjectBuild();
      new Thread(this).start();
    } else {
      String errorMsg = "Data recording ongoing. No other startup allowed!";
      LOG.info(errorMsg);
      throw new RuntimeException(errorMsg);
    }
  }

  public void startupFromWebConfiguration(int uptime, int timeout, JSONArray sensorConfiguration) throws RuntimeException {
    if (!this.running) {
      this.uptime = uptime;
      this.timeout = timeout;
      initFromWebapplication(sensorConfiguration);
      new Thread(this).start();
    } else {
      String errorMsg = "Data recording ongoing. No other startup allowed!";
      LOG.info(errorMsg);
      throw new RuntimeException(errorMsg);
    }
  }

  @Override
  public void run() {
    sleep(this.uptime);
    shutdown();
    this.running = false;
  }

  private void initFromProjectBuild() {
    LOG.info("initialise");
    this.swList = this.swFactory.setupFromProjectBuildConfiguration();

    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.connectToSensor(this.timeout);
    }
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.enableLogging();
    }

    LOG.info("initialisation complete");
    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.ledControlScriptFileName);
  }

  private void initFromWebapplication(JSONArray sensorConfiguration) {
    LOG.info("initialise");
    this.swList = this.swFactory.setupFromWebinterfaceConfinguration(sensorConfiguration);

    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.connectToSensor(this.timeout);
    }
    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.enableLogging();
    }

    LOG.info("initialisation complete");
    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.ledControlScriptFileName);
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
