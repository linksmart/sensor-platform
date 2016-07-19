package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.control.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;

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

  public void connect(int timeout) throws BluetoothGattException;

  public void disconnectBlocking();

  public void disconnect();

  public void exitGatttool();

}
