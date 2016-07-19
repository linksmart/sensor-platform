package de.fhg.fit.biomos.sensorplatform.control;

public interface SensorWrapper extends SensorNotificationDataObserver {

  public void connectToSensor(int timeout);

  public void enableLogging();

  public void disableLogging();

  public void disconnectBlocking();

  public void disconnect();

  public void shutdown();

}
