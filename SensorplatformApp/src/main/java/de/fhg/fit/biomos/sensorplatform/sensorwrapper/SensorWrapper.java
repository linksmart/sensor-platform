package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolSecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

public interface SensorWrapper {

  public String getBDaddress();

  public SensorName getDeviceName();

  public Gatttool.State getGatttoolInternalState();

  public void setSecurityLevel(GatttoolSecurityLevel secLevel);

  public boolean connectToSensorBlocking(int timeout);

  public void reconnectToSensor();

  public void enableLogging();

  public void disableLogging();

  public void disconnectBlocking();

  public void disconnect();

  public void shutdown();

}
