package de.fhg.fit.biomos.sensorplatform.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock control class for hciconfig for using the Windows installation of the sensorplatform. On Windows there is no hciconfig.
 *
 * @author Daniel Pyka
 *
 */
public class HciconfigVirtual implements Hciconfig {

  private static final Logger LOG = LoggerFactory.getLogger(HciconfigVirtual.class);

  public HciconfigVirtual() {
  }

  @Override
  public String getLocalBDaddress() {
    return "11:22:33:44:55:66";
  }

  @Override
  public void up() {
    LOG.info("fake controller up");
  }

  @Override
  public void down() {
    LOG.info("fake controller down");
  }

}
