package de.fhg.fit.biomos.sensorplatform.control;

public interface SensorNotificationDataObserver {
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues);
}
