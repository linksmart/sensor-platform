package de.fhg.fit.biomos.sensorplatform.system;

import java.net.SocketException;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;

/**
 * Functionality for the Sensorplatform running on a Cubieboard 3. Most of the stuff is NYI.
 *
 * @author Daniel Pyka
 *
 */
public class Cubieboard3 implements HardwarePlatform {

  public Cubieboard3() {
    // TODO Auto-generated constructor stub
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
    return new HciconfigImpl();
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
