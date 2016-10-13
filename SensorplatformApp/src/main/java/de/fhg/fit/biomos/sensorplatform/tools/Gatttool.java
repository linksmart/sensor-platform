package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;

/**
 * Defines a hardware abstraction layer to interact with the gatttool command line tool from Bluez.
 *
 * @author Daniel Pyka
 *
 */
public interface Gatttool extends Runnable {

  public enum State {
    CONNECTED, DISCONNECTED, RECONNECTING
  };

  public enum Mode {
    COMMANDMODE, NOTIFICATION
  }

  /**
   * Expose the internal state of the Gatttool. SensorOverseer makes use of it.
   *
   * @return State the state the gatttool is currently in
   */
  public State getInternalState();

  public Mode getInternalMode();

  public void setInternalMode(Mode mode);

  /**
   * Expose process input stream to be used in a sensorobject.
   *
   * @return stream to the gatttool process
   */
  public BufferedWriter getStreamToSensor();

  /**
   * Set the observer.
   *
   * @param observer
   *          SensorWrapper is the observer
   */
  public void setObserver(AbstractSensorWrapper<?> observer);

  /**
   * Try to connect to a sensor.
   *
   * @param timeout
   *          int number of seconds to try connecting
   * @return true if connected successfully, false otherwise
   */
  public boolean connectBlocking(int timeout);

  /**
   * Try to connect to a sensor again, if gatttool has lost the previous connection (maybe because sensor went offline).
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
