package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

/**
 *
 * @author Daniel Pyka
 *
 */
public class ObservableSensorNotificationData {
  private SensorNotificationDataObserver observer;

  public void setObserver(SensorNotificationDataObserver observer) {
    this.observer = observer;
  }

  public void notifyObserver(String handle, String rawHexValues) {
    this.observer.newNotificationData(this, handle, rawHexValues);

  }
}
