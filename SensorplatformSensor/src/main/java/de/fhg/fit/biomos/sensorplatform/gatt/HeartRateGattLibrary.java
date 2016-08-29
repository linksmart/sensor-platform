package de.fhg.fit.biomos.sensorplatform.gatt;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface HeartRateGattLibrary extends GattLibrary {

  public String getHandleHeartRateMeasurement();

  public String getHandleHeartRateNotification();

}
