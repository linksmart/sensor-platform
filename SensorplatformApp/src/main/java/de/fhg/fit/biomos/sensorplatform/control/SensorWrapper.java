package de.fhg.fit.biomos.sensorplatform.control;

import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;

public interface SensorWrapper {

  /**
   * Expose the sensor for the SensorObserver.
   *
   * @return
   */
  public Sensor getSensor();

  public boolean connectToSensor(int timeout);

  public boolean reconnectToSensor(int timeout);

  public void enableLogging();

  public void disableLogging();

  public void disconnectBlocking();

  public void disconnect();

  public void shutdown();

}
