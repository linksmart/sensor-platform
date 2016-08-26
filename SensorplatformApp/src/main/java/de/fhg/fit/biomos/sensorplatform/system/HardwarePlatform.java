package de.fhg.fit.biomos.sensorplatform.system;

public interface HardwarePlatform {

  public void setLEDstateSTANDBY();

  public void setLEDstateRECORDING();

  public void setLEDstateERROR();

  public void connectToMobileInternet();

}
