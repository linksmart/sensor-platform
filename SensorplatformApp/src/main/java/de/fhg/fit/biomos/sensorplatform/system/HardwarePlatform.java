package de.fhg.fit.biomos.sensorplatform.system;

import java.net.SocketException;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;

/**
 * Defines functions which are dependant on the board hardware or low level configuration of the operating system. Is used as a factory for creating specific
 * gatttools.
 *
 * @author Daniel Pyka
 *
 */
public interface HardwarePlatform {

  /**
   * Set a LED on the board to blink slowly.
   */
  public void setLEDstateSTANDBY();

  /**
   * Set a LED on the board to blink in a "Heartbeat" pattern.
   */
  public void setLEDstateRECORDING();

  /**
   * Turn off a LED on the board.
   */
  public void setLEDstateERROR();

  /**
   * Get the local Bluetooth controller.
   *
   * @return Hciconfig object for interacting with hciconfig console tool
   */
  public Hciconfig getBluetoothController();

  /**
   * Get the hcitool command line tool.
   *
   * @return Hcitool object for interacting with hcitool console tool
   */
  public Hcitool getHcitool();

  /**
   * Print infos about the interface which is used by the mobile internet connection.
   *
   * @return true if it is up, false otherwise
   * @throws SocketException
   *           if interface is not up and running
   * @throws NullPointerException
   *           if interface is not up and running
   */
  public boolean printInternetInterfaceInfo() throws SocketException, NullPointerException;

  /**
   * Use some driver to start a daemon process which handles mobile internet connection.
   */
  public void connectToMobileInternet();

}
