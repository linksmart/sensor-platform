package de.fhg.fit.biomos.sensorplatform.gatt;

/**
 * This interface defines two methods which will return the required handles to enable pulse oximeter notifications and data interpretation. The API may be
 * expanded in the future for additional data.
 *
 * @author Daniel Pyka
 *
 */
public interface PulseOximeterGattLibrary extends GattLibrary {

  /**
   * Retrieve the handle address of the sensor for pulse oximeter data. This address can be checked if the incoming notification really is a pulse oximeter
   * notification.
   *
   * @return handle address of pulse oximeter data
   */
  public String getHandlePulseOximeterMeasurement();

  /**
   * Retrieve the handle address which is used to enable/ disable pulse oximeter notifications.
   *
   * @return handle address to enable/ disable pulse oximeter notifications
   */
  public String getHandlePulseOximeterNotification();

}
