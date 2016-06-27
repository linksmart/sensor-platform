package de.fhg.fit.biomos.sensorplatform.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sensors.CC2650;
import de.fhg.fit.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static final File logDirectory = new File("/home/pi/Sensorplatform/logs");

  public static void main(String[] args) {
    LOG.info("start");
    new Main().run();
    LOG.info("end");
  }

  public Main() {
    //
  }

  public void setupFromConfig() {
    // TODO
  }

  public void run() {
    try {
      Sensor cc2650 = new CC2650("SensorTagCC2650", CC2650lib.DEFAULT_BDADDRESS);
      Sensor polarH7 = new PolarH7("Polar H7", PolarH7lib.DEFAULT_BDADDRESS);

      Gatttool gatt1 = new GatttoolImpl(cc2650, false);
      Gatttool gatt2 = new GatttoolImpl(polarH7, false);

      Thread t1 = new Thread(gatt1);
      Thread t2 = new Thread(gatt2);
      t1.start();
      t2.start();

      gatt1.connect();
      gatt2.connect();
      gatt1.enableLogging();
      gatt2.enableLogging();
      Thread.sleep(10000);
      gatt1.disableLogging();
      gatt2.disableLogging();
      gatt1.disconnectAndExit();
      gatt2.disconnectAndExit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
