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
   * Get the rssi value from the surf stick.
   *
   * @return int signal strength rssi in dBm
   */
  public int getOverallRSSIfromMobileInternet();

  /**
   * Get the rscp value from the surf stick.
   *
   * @return int rscp value in dBm
   */
  public int getRSCPfromMobileInternet();

  /**
   * Get the Ec/Io value from the surf stick.
   *
   * @return int Ec/Io in dB
   */
  public int getECIOfromMobileInternet();

  /**
   * Use various parameters to determine, if the surf stick is able to upload the heart rate data power efficient. This method is intended to set an internal
   * upload permission flag in the concrete implementation of hardware platform.
   */
  public void evaluateSignalQuality();

  /**
   * Return the value of the internal upload permission flag.
   *
   * @return true in case of good mobile internet signal quality, false otherwise
   */
  public boolean isUploadPermitted();

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
