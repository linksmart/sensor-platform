package de.fhg.fit.biomos.sensorplatform.system;

/**
 * Funtions that is OS or board dependent.
 *
 * @author Daniel Pyka
 *
 */
public interface HardwarePlatform {

  public void setLEDstateSTANDBY();

  public void setLEDstateRECORDING();

  public void setLEDstateERROR();

  public void connectToMobileInternet();

}
