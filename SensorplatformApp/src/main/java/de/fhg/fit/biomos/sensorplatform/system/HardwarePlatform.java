package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

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
   * Get the last bean with signal quality parameters.
   *
   * @return SignalQualityBean bean
   */
  public SignalQualityBean getSignalQualityBean();

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
   * Use some driver to start a daemon process which handles mobile internet connection.
   */
  public void connectToMobileInternet();

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
   * Get the hcitool command line tool.
   *
   * @return Hcitool object for interacting with hcitool console tool
   */
  public Hcitool getHcitool();

}
