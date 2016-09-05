package de.fhg.fit.biomos.sensorplatform.system;

import java.net.SocketException;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;

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

  public Hciconfig getBluetoothController();

  public void printInternetInterfaceInfo() throws SocketException, NullPointerException;

  public void connectToMobileInternet();

}
