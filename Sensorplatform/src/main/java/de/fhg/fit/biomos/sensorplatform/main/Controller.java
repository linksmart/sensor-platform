package de.fhg.fit.biomos.sensorplatform.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.sensors.SensorFactory;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

/**
 *
 * @author Daniel Pyka
 *
 */
public class Controller {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private final Properties properties;

  private final List<Gatttool> gatttoolList = new ArrayList<Gatttool>();

  private final List<Sensor> sensorList;

  private final LEDcontrol ledcontrol;

  public Controller(Properties properties) {
    this.properties = properties;

    SensorFactory sensorFactory = new SensorFactory(this.properties);
    this.sensorList = sensorFactory.createSensorsFromConfigurationFile();

    this.ledcontrol = new LEDcontrol(properties);
  }

  public void startup() {
    for (Sensor sensor : this.sensorList) {
      System.out.println(sensor);
      GatttoolImpl gatttool = new GatttoolImpl(sensor);
      this.gatttoolList.add(gatttool);

      new Thread(gatttool).start();
      gatttool.connect();
    }
    LOG.info("gatttool(s) connected");

    for (Gatttool gatttool : this.gatttoolList) {
      gatttool.enableLogging();
    }
    LOG.info("logging enabled");
    this.ledcontrol.setLED(LEDstate.HEARTBEAT);
  }

  public void shutdown() {
    for (Gatttool gatttool : this.gatttoolList) {
      gatttool.disableLogging();
      gatttool.disconnectAndExit();
    }
    LOG.info("shutdown");
    this.ledcontrol.setLED(LEDstate.BLINK);
  }

}
