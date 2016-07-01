package de.fhg.fit.biomos.sensorplatform.web;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface HttpUploader {

  public void login();

  public void sendData(String bdAddress, String quantityType, String value, String unit);

}
