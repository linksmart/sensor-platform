package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

/**
 * Defines an Observer which is notified by another class.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorNotificationDataObserver {

  /**
   * Get a meaningful time stamp when the last notification has arrived.
   *
   * @return long time stamp milliseconds
   */
  public long getLastNotifactionTimestamp();

  /**
   * Notifiy the observer when a new sensor notification has arrived.
   *
   * @param observable
   *          source object of this notification
   * @param handle
   *          hexadecimal handle number of the notification as an identifier
   * @param rawHexValues
   *          hexadecimal values of the notification delivered by gatttool
   */
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues);
}
