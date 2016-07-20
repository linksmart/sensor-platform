package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final String propertiesFilename = "SensorplatformApp.properties";

  private final Properties properties = new Properties();

  public static void main(String[] args) {
    new Main().start();
  }

  public Main() {
    try {
      this.properties.load(ClassLoader.getSystemResourceAsStream(propertiesFilename));
      LOG.info("version is " + this.properties.getProperty("version"));
    } catch (IOException e) {
      LOG.error("cannot load properties");
      System.exit(1);
    }
    // download samples
    // DITGuploader ditGhttpUploader = new DITGuploader(this.properties);
    // ditGhttpUploader.login();
    // ditGhttpUploader.downloadData();
    // System.exit(0);
  }

  private void start() {
    try {
      Controller controller = new Controller(this.properties);
      controller.startup();

      int uptime = new Integer(this.properties.getProperty("sensorplatform.uptime.seconds"));
      LOG.info("sleeping for " + uptime + " seconds");
      Thread.sleep(uptime * 1000);

      controller.shutdown();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
