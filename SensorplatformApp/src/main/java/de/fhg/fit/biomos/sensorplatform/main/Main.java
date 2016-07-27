package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.web.ServerStarter;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final String propertiesFileName = "SensorplatformApp.properties";

  private final Properties properties = new Properties();

  public static void main(String[] args) {
    new Main().start();
  }

  public Main() {
    try {
      this.properties.load(ClassLoader.getSystemResourceAsStream(propertiesFileName));
      LOG.info("version is " + this.properties.getProperty("version"));
    } catch (IOException e) {
      LOG.error("cannot load properties");
      System.exit(1);
    }
  }

  private void start() {
    new Thread(new ServerStarter(this.properties)).start();
    // if (Boolean.parseBoolean(this.properties.getProperty("run.with.default.settings"))) {
    // new Controller(this.properties).startupFromProjectBuildConfiguration(10);
    // }
  }

  // @Deprecated
  // private void downloadSamples() {
  // DITGuploader ditGhttpUploader = new DITGuploader(this.properties);
  // ditGhttpUploader.login();
  // ditGhttpUploader.downloadData();
  // System.exit(0);
  // }

}
