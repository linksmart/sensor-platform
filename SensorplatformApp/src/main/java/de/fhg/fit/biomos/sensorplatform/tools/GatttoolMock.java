package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.ObservableSensorNotificationData;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 * Used for windows for testing.
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolMock extends ObservableSensorNotificationData implements Gatttool {

  public GatttoolMock() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }

  @Override
  public State getInternalState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addObs(SensorNotificationDataObserver sndo) {
    // TODO Auto-generated method stub

  }

  @Override
  public BufferedWriter getStreamToSensor() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SecurityLevel getSecurityLevel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AddressType getAddressType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean connectBlocking(int timeout) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void reconnect() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disconnectBlocking() {
    // TODO Auto-generated method stub

  }

  @Override
  public void exitGatttool() {
    // TODO Auto-generated method stub

  }

}
