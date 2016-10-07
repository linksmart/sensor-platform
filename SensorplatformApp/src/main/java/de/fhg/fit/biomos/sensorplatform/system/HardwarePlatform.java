package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;

/**
 * Defines functions which are dependant on the board hardware or low level configuration of the operating system.
 *
 * @author Daniel Pyka
 *
 */
public interface HardwarePlatform extends Runnable {

  /**
   * Check if the Sensorplatform is connected to mobile internet via a surf stick.
   *
   * @return true if connection is active, false otherwise
   */
  public boolean isConnectedToMobileInternet();

  /**
   * Get the signal quality from surf stick.
   *
   * @return int signal quality in dBm
   */
  public int getMobileInternetSignalQuality();

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
   * Use some driver to start a daemon process which handles mobile internet connection.
   */
  public void connectToMobileInternet();

}
