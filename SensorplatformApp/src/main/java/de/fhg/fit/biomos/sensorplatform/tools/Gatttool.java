package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolSecurityLevel;

/**
 * Controller class for gatttool command line tool. You may use multiple instances of it at once.<br />
 * Gatttool is an interactive(!) tool for Bluetooth Low Energy funtions.
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

  public void removeObs();

  public BufferedWriter getStreamToSensor();

  public GatttoolSecurityLevel getSecurityLevel();

  public void setSecurityLevel(GatttoolSecurityLevel secLevel);

  public boolean connectBlocking(int timeout);

  public void reconnect();

  public void disconnectBlocking();

  public void disconnect();

  public void exitGatttool();

}
