package de.fhg.fit.biomos.sensorplatform.control;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

/**
 * This class defines the control flow of the sensorplatform.
 *
 * @author Daniel Pyka
 *
 */
public class Controller {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private final Properties properties;

  private List<SensorWrapper> swList;

  public Controller(Properties properties) {
    this.properties = properties;
  }

  /**
   * Initialise all components and activate sensor measurements.
   */
  public void startup() {
    LOG.info("startup");
    this.swList = SensorWrapperFactory.setupFromProjectBuildConfiguration(this.properties);

    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.connectToSensor(new Integer(this.properties.getProperty("sensor.connect.timeout.seconds")));
    }

    for (SensorWrapper sensorWrapper : this.swList) {
      sensorWrapper.enableLogging();
    }

    LOG.info("initialisation complete");

    ShellscriptExecutor.setLED(LEDstate.RUNNING, this.properties.getProperty("led.control.script"));
  }

  /**
   * Shut down sub-processes and threads of the sensorplatform <b>gracefully</b>. This avoids zombie gatttool processes which may prevent sensors from returning
   * to standby mode and other <b>bad</b> things!
   */
  public void shutdown() {
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
    LOG.info("shutdown");
    ShellscriptExecutor.setLED(LEDstate.STANDBY, this.properties.getProperty("led.control.script"));
  }

}
