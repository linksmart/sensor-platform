package de.fhg.fit.biomos.sensorplatform.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HciconfigMock implements Hciconfig {

  private static final Logger LOG = LoggerFactory.getLogger(HciconfigMock.class);

  public HciconfigMock() {
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
