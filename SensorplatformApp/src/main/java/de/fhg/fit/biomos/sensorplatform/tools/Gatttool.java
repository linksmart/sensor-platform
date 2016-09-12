package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 * Controller class for gatttool command line tool. You may use multiple instances of it at once. Gatttool is an interactive(!) tool for Bluetooth Low Energy
 * funtions.
 *
 * @author Daniel Pyka
 *
 */
public interface Gatttool extends Runnable {

  enum State {
    CONNECTED, DISCONNECTED, RECONNECTING
  };

  public State getInternalState();

  public void addObs(SensorNotificationDataObserver sndo);

  public BufferedWriter getStreamToSensor();

  public SecurityLevel getSecurityLevel();

  public AddressType getAddressType();

  public boolean connectBlocking(int timeout);

  public void reconnect();

  public void disconnectBlocking();

  public void exitGatttool();

}
