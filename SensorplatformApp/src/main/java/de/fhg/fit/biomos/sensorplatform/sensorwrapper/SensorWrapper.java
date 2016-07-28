package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

public interface SensorWrapper {

  public Gatttool.State getGatttoolInternalState();

  public boolean connectToSensorBlocking(int timeout);

  public void reconnectToSensor();

  public void enableLogging();

  public void disableLogging();

  public void disconnectBlocking();

  public void disconnect();

  public void shutdown();

}
