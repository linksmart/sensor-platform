package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

/**
 * Defines a observable class which send notifications to the observer.
 *
 * @author Daniel Pyka
 *
 */
public class ObservableSensorNotificationData {

  private SensorNotificationDataObserver observer;

  /**
   * Set the observer.
   *
   * @param observer
   *          SensorWrapper is the observer
   */
  public void setObserver(SensorNotificationDataObserver observer) {
    this.observer = observer;
  }

  /**
   * Notify the SensorWrapper that a new notification has arrived in Gatttool.
   *
   * @param handle
   *          handle address of the notification
   * @param rawHexValues
   *          hexadecimal values of the notification
   */
  public void notifyObserver(String handle, String rawHexValues) {
    this.observer.newNotificationData(this, handle, rawHexValues);
  }

}
