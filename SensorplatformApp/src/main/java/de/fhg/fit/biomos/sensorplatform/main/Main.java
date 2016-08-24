package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.guice.SensorplatformServletConfig;
import de.fhg.fit.biomos.sensorplatform.web.ServerStarter;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final String propertiesFileName = "SensorplatformApp.properties";

  private final Properties properties = new Properties();

  public static void main(String[] args) {
    Main sensorplatform = new Main();
    sensorplatform.startWebServer();
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

  private void startWebServer() {
    SensorplatformServletConfig sensorplatformServletConfig = new SensorplatformServletConfig(this.properties);
    ServerStarter serverstarter = new ServerStarter(this.properties, sensorplatformServletConfig);
    serverstarter.start();
  }

}
