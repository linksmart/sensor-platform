package de.fhg.fit.biomos.sensorplatform.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.sensors.SensorFactory;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final String propertiesFilename = "Sensorplatform.properties";

  private final Properties properties = new Properties();

  public static void main(String[] args) {
    new Main().start();
  }

  /**
   * TODO dependency injection for properties
   */
  public Main() {

    try {
      this.properties.load(ClassLoader.getSystemResourceAsStream(propertiesFilename));
    } catch (IOException e) {
      LOG.error("cannot load properties");
      System.exit(1);
    }

    LOG.info("version is " + this.properties.getProperty("version"));
    File sensorsDataDirectory = new File(this.properties.getProperty("sensors.data.directory"));
    LOG.info("data directory is " + sensorsDataDirectory.getAbsolutePath());
    if (!sensorsDataDirectory.exists()) {
      sensorsDataDirectory.mkdir();
      LOG.info("data directory created");
    }
  }

  private void start() {
    try {
      SensorFactory sensorFactory = new SensorFactory(this.properties);

      List<Sensor> sensorList = sensorFactory.createSensorsFromConfigurationFile();
      List<Gatttool> gatttoolList = new ArrayList<Gatttool>();

      for (Sensor sensor : sensorList) {
        System.out.println(sensor);
        GatttoolImpl gatttool = new GatttoolImpl(sensor);
        gatttoolList.add(gatttool);

        new Thread(gatttool).start();
        gatttool.connect();
      }

      for (Gatttool gatttool : gatttoolList) {
        gatttool.enableLogging();
      }

      Thread.sleep(5000);

      for (Gatttool gatttool : gatttoolList) {
        gatttool.disableLogging();
        gatttool.disconnectAndExit();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
