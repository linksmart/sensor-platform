package de.fhg.fit.biomos.sensorplatform.web;

/**
 * If there are plans to support additional webinterface in the sensorplatform in the future, this interface may become useful. Username, password, cookies and
 * all other data should be stored in the class which implements this interface. Since there is only one webinterface with a fixed, predefined workflow, this
 * interface may need to be changed to properly fit for connection to different webinterfaces.
 *
 * @author Daniel Pyka
 *
 */
public interface Uploader {

  /**
   * Log in with previously stored user name and password. A cookie or something similar is expected in return as a login session which is stored and used
   * further.
   */
  public void login();

  /**
   * Send a single sample and some metadata to the webinterface.
   *
   * @param bdAddress
   *          Bluetooth device address of the sensor (metadata)
   * @param quantityType
   *          e.g. HeartRate
   * @param value
   *          e.g. 60
   * @param unit
   *          e.g. bpm
   */
  public void sendData(String bdAddress, String quantityType, String value, String unit);

}
