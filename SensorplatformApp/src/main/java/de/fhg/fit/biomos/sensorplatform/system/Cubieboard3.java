package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;

/**
 * Functionality for the Sensorplatform running on a Cubieboard 3. Most of the stuff is NYI.
 *
 * @author Daniel Pyka
 *
 */
public class Cubieboard3 implements HardwarePlatform {

  // private static final Logger LOG = LoggerFactory.getLogger(Cubieboard3.class);

  public Cubieboard3() {
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isConnectedToMobileInternet() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getRSSIfromMobileInternet() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getRSCPfromMobileInternet() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getECIOfromMobileInternet() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void evaluateSignalQuality() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isUploadPermitted() {
    // TODO Auto-generated method stub
    return false;
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
    return null;
  }

  @Override
  public Hcitool getHcitool() {
    return null;
  }

  @Override
  public void connectToMobileInternet() {
    // TODO Auto-generated method stub

  }
}
