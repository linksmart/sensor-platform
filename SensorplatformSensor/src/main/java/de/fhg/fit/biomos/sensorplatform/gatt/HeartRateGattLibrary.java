package de.fhg.fit.biomos.sensorplatform.gatt;

/**
 * This interface defines two methods which will return the required handles to enable heart rate notifications and data interpretation. The API may be expanded
 * in the future for additional data.
 *
 * @author Daniel Pyka
 *
 */
public interface HeartRateGattLibrary extends GattLibrary {

  /**
   * Retrieve the handle address of the sensor for heart rate data. This address can be checked if the incoming notification really is a heart rate
   * notification.
   *
   * @return handle address of heart rate data
   */
  public String getHandleHeartRateMeasurement();

  /**
   * Retrieve the handle address which is used to enable/ disable heart rate notifications.
   * 
   * @return handle address to enable/ disable heart rate notifications
   */
  public String getHandleHeartRateNotification();

}
