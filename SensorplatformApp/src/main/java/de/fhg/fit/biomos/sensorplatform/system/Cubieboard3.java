package de.fhg.fit.biomos.sensorplatform.system;

import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;

/**
 * Functionality for the Sensorplatform running on a Cubieboard 3. Most of the stuff is NYI.
 *
 * @author Daniel Pyka
 *
 */
public class Cubieboard3 implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(Cubieboard3.class);

  private final Hciconfig hciconfig;
  private final Hcitool hcitool;

  public Cubieboard3() {
    this.hciconfig = new HciconfigImpl();
    this.hcitool = new HcitoolImpl();
  }

  @Override
  public void setLEDstateSTANDBY() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setLEDstateRECORDING() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setLEDstateERROR() {
    // TODO Auto-generated method stub

  }

  @Override
  public Hciconfig getBluetoothController() {
    return this.hciconfig;
  }

  @Override
  public Hcitool getHcitool() {
    return this.hcitool;
  }

  @Override
  public boolean printInternetInterfaceInfo() throws SocketException, NullPointerException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void connectToMobileInternet() {
    // TODO Auto-generated method stub

  }
}
