package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

public interface SensorNotificationDataObserver {

  public long getLastNotifactionTimestamp();

  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues);
}
