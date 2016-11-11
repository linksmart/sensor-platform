package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

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
  public int getOverallRSSIfromMobileInternet() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public SignalQualityBean getSignalQualityBean() {
    // TODO Auto-generated method stub
    return null;
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
  public Hcitool getHcitool() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void connectToMobileInternet() {
    // TODO Auto-generated method stub

  }
}
