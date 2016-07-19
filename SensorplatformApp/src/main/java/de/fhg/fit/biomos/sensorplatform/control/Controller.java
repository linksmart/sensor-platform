package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
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

  private final List<Gatttool> gatttoolList = new ArrayList<Gatttool>();

  private final LEDcontrol ledcontrol;

  public Controller(Properties properties) {
    this.properties = properties;
    this.ledcontrol = new LEDcontrol(properties);
  }

  /**
   * Initialise all components and activate sensor measurements.
   */
  public void startup() {
    SensorWrapperFactory sensorFactory = new SensorWrapperFactory(this.properties);

    // for (Sensor sensor : sensorFactory.createSensorsFromConfigurationFile()) {
    // System.out.println("Sensor: " + sensor);
    // GatttoolImpl gatttool = new GatttoolImpl(sensor.getAddressType(), sensor.getBdaddress());
    // this.gatttoolList.add(gatttool);
    //
    // new Thread(gatttool).start();
    // try {
    // gatttool.connect(new Integer(this.properties.getProperty("sensor.connect.timeout.seconds")));
    // } catch (BluetoothGattException e) {
    // LOG.error(e.getMessage());
    // // gatttool.disconnectAndExit();
    // LOG.info("Invalid gatttool removed from list: " + this.gatttoolList.remove(gatttool));
    // }
    // }

    // for (Gatttool gatttool : this.gatttoolList) {
    // gatttool.enableLogging();
    // }

    this.ledcontrol.setLED(LEDstate.HEARTBEAT);
  }

  /**
   * Shut down sub-processes and threads of the sensorplatform <b>gracefully</b>. This avoids zombie gatttool processes which may prevent sensors from returning
   * to standby mode and other <b>bad</b> things!
   */
  public void shutdown() {
    for (Gatttool gatttool : this.gatttoolList) {
      // gatttool.disableLogging();
      // gatttool.disconnectAndExit();
    }
    LOG.info("shutdown");
    this.ledcontrol.setLED(LEDstate.BLINK);
  }

}
