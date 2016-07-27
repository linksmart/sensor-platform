package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.control.SensorNotificationDataObserver;

/**
 * Controller class for gatttool command line tool. You may use multiple instances of it at once.<br />
 * Gatttool is an interactive(!) tool for Bluetooth Low Energy funtions.
 *
 * @author Daniel Pyka
 *
 */
public interface Gatttool extends Runnable {

  public void addObs(SensorNotificationDataObserver sndo);

  public void removeObs();

  BufferedWriter getStreamToSensor();

  public boolean connect(int timeout);

  public boolean reconnect(int timeout);

  public void disconnectBlocking();

  public void disconnect();

  public void exitGatttool();

}
