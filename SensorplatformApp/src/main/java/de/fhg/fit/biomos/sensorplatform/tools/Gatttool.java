package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorNotificationDataObserver;

/**
 * Controller class for gatttool command line tool from Bluez. You use one Gatttool per Sensor. Gatttool is an interactive(!) tool for Bluetooth Low Energy Gatt
 * functions.
 *
 * @author Daniel Pyka
 *
 */
public interface Gatttool extends Runnable {

  enum State {
    CONNECTED, DISCONNECTED, RECONNECTING
  };

  /**
   * Expose the internal state of the Gatttool. SensorOverseer makes use of it.
   *
   * @return State the state the gatttool is currently in
   */
  public State getInternalState();

  /**
   * FIXME remove
   *
   * @param abstractSensorWrapper
   *          the observer
   */
  public void addObs(SensorNotificationDataObserver abstractSensorWrapper);

  /**
   * Expose process input stream to be used in a sensorobject.
   *
   * @return stream to the gatttool process
   */
  public BufferedWriter getStreamToSensor();

  /**
   * Try to connect to a sensor.
   *
   * @param timeout
   *          int number of seconds to try connecting
   * @return true if connected successfully, false otherwise
   */
  public boolean connectBlocking(int timeout);

  /**
   * Try to connect again to a sensor, if gatttool has lost the previous connection (maybe because sensor went offline).
   */
  public void reconnect();

  /**
   * Disconnect gracefully from the sensor.
   */
  public void disconnect();

  /**
   * Exit gatttool gracefully to properly shut down the process and release all resources.
   */
  public void exitGatttool();

}
