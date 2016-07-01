package de.fhg.fit.biomos.sensorplatform.main;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.web.DITGhttpUploader;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static String propertiesFilename = "Sensorplatform.properties";

  public static File sensorsDataDirectory = null;
  public static String timestampFormat = null;

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
    timestampFormat = properties.getProperty("ditg.webinterface.timestamp.format");
    LOG.info("time stamp pattern " + timestampFormat);
    sensorsDataDirectory = new File(properties.getProperty("sensors.data.directory"));
    LOG.info("data directory is " + sensorsDataDirectory.getAbsolutePath());
    if (!sensorsDataDirectory.exists()) {
      sensorsDataDirectory.mkdir();
      LOG.info("directory created");
    }
  }

  private void start() {
    DITGhttpUploader ditg = new DITGhttpUploader(properties);
    ditg.login();
    ditg.sendData("A0:E6:F8:B6:37:05", "HeartRate", "90", "bpm");
    ditg.downloadData();
    // SensorFactory sensorFactory = new SensorFactory(properties.getProperty("sensors.description.file"));

    // List<Sensor> sensors = sensorFactory.createSensorsFromConfigurationFile();

    // for (Sensor sensor : sensors) {
    // System.out.println(sensor);
    // }

    try {
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
