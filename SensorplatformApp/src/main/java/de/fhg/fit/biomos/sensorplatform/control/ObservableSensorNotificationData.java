package de.fhg.fit.biomos.sensorplatform.control;

public class ObservableSensorNotificationData {
  private SensorNotificationDataObserver observer;

  public void setObserver(SensorNotificationDataObserver observer) {
    this.observer = observer;
  }

  public void deleteObserver() {
    this.observer = null;
  }

  public void notifyObserver(String handle, String rawHexValues) {
    this.observer.newNotificationData(this, handle, rawHexValues);

  }
}
