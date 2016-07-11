package de.fhg.fit.biomos.sensorplatform.main;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    // download samples
    // DITGhttpUploader ditGhttpUploader = new DITGhttpUploader(this.properties);
    // ditGhttpUploader.login();
    // ditGhttpUploader.downloadData();
    // System.exit(0);

    LOG.info("version is " + this.properties.getProperty("version"));
    File sensorsDataDirectory = new File(this.properties.getProperty("sensor.data.directory"));
    LOG.info("data directory is " + sensorsDataDirectory.getAbsolutePath());
    if (!sensorsDataDirectory.exists()) {
      sensorsDataDirectory.mkdir();
      LOG.info("data directory created");
    }
  }

  private void start() {
    try {
      Controller controller = new Controller(this.properties);
      controller.startup();

      Thread.sleep(new Integer(this.properties.getProperty("sensorplatform.uptime.seconds")) * 1000);

      controller.shutdown();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
