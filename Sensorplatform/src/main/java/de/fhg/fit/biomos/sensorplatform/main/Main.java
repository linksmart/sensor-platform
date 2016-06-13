package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SensorDescription;

/**
 * @author Daniel Pyka
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    LOG.info("start");
    new Main().run();
    LOG.info("end");
  }

  public Main() {
    //
  }

  public void run() throws IOException, InterruptedException {
    HcitoolImpl hcitool = new HcitoolImpl();
    hcitool.collectLocalDeviceAndAddress();
    // hcitool.lescan(5);
    hcitool.exit();

    GatttoolImpl gatttool = new GatttoolImpl(new SensorDescription("FAROS-1620509", "EC:FE:7E:15:D5:EB"));
    gatttool.start();
    gatttool.listPrimaryServices();
    gatttool.exit();
  }

}
