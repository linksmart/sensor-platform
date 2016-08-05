package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.guice.SensorplatformServletConfig;
import de.fhg.fit.biomos.sensorplatform.web.ServerStarter;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final String propertiesFileName = "SensorplatformApp.properties";

  private final Properties properties = new Properties();

  private Injector injector;

  public static void main(String[] args) {
    Main sensorplatformApp = new Main();
    sensorplatformApp.startWebServer();
    sensorplatformApp.checkLastState();
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
    new Thread(new ServerStarter(this.properties, sensorplatformServletConfig)).start();
    while ((this.injector = sensorplatformServletConfig.getCreatedInjector()) == null) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    LOG.info("injector ready to use");
  }

  private void checkLastState() {
    LOG.info("checking the last state of the sensorplatform");
    this.injector.getInstance(Controller.class).checkLastSensorplatformState();
    LOG.info("main startup done");
  }

}
