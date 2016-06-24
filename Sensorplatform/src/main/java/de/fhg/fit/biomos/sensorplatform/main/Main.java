package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sensors.CC2650;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    LOG.info("start");
    new Main().run();
    LOG.info("end");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {

      }
    });
  }

  public Main() {
    //
  }

  public void setupFromConfig() {
    // TODO
  }

  // public void scan() {
  // try {
  // HcitoolImpl hcitool = new HcitoolImpl();
  // hcitool.collectLocalDeviceAndAddress();
  // hcitool.scan();
  // hcitool.exit();
  // } catch (IOException | InterruptedException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // public void lescan() {
  // try {
  // HcitoolImpl hcitool = new HcitoolImpl();
  // hcitool.collectLocalDeviceAndAddress();
  // hcitool.lescan(5);
  // hcitool.exit();
  // } catch (IOException | InterruptedException e) {
  // e.printStackTrace();
  // }
  // }

  public void run() throws IOException, InterruptedException {
    // HcitoolImpl hcitool = new HcitoolImpl();
    // hcitool.collectLocalDeviceAndAddress();
    // hcitool.lescan(5);
    // hcitool.exit();

    Sensor cc2650 = new CC2650("Texas Instruments CC2650 SensorTag", CC2650lib.DEFAULT_BDADDRESS);

    GatttoolImpl gt = new GatttoolImpl(cc2650);

    gt.setup();
    gt.enableLogging();
    Thread.sleep(5000);
    gt.disableLogging();
    Thread.sleep(500);
    gt.closeGracefully();
  }

}
