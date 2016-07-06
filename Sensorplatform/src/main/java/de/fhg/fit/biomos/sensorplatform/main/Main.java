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

  private static final Properties properties = new Properties();

  public static void main(String[] args) {
    new Main().start();
  }

  /**
   * TODO dependency injection for properties
   */
  public Main() {

    try {
      properties.load(ClassLoader.getSystemResourceAsStream(propertiesFilename));
    } catch (IOException e) {
      LOG.error("cannot load properties");
      System.exit(1);
    }

    LOG.info("version is " + properties.getProperty("version"));
    File sensorsDataDirectory = new File(properties.getProperty("sensors.data.directory"));
    LOG.info("data directory is " + sensorsDataDirectory.getAbsolutePath());
    if (!sensorsDataDirectory.exists()) {
      sensorsDataDirectory.mkdir();
      LOG.info("directory created");
    }
  }

  private void start() {
    try {
      SensorFactory sensorFactory = new SensorFactory(properties);

      List<Sensor> sensorList = sensorFactory.createSensorsFromConfigurationFile();
      List<Gatttool> gatttoolList = new ArrayList<Gatttool>();
      // List<Thread> threads = new ArrayList<Thread>();

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

    // try {
    // Sensor cc2650 = new CC2650(SensorName.CC2650, CC2650lib.DEFAULT_BDADDRESS, AddressType.PUBLIC, SensorType.GENERALPURPOSE, new SensorConfiguration()
    // .addSetting("irtemperature", CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT).addSetting("humidity", CC2650lib.INTERVAL_HUMIDITY_1000MS_DEFAULT));

    // Sensor polarH7 = new PolarH7(SensorName.PolarH7, PolarH7lib.DEFAULT_BDADDRESS, AddressType.PUBLIC, SensorType.HRM);

    // Gatttool gatt1 = new GatttoolImpl(sensors.get(1));
    // Gatttool gatt2 = new GatttoolImpl(sensors.get(0));
    //
    // Thread t1 = new Thread(gatt1);
    // Thread t2 = new Thread(gatt2);
    // t1.start();
    // t2.start();
    //
    // gatt1.connect();
    // gatt2.connect();
    // gatt1.enableLogging();
    // gatt2.enableLogging();
    // Thread.sleep(5000);
    // gatt1.disableLogging();
    // gatt2.disableLogging();
    // gatt1.disconnectAndExit();
    // gatt2.disconnectAndExit();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
  }

}
