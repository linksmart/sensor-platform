package de.fhg.fit.biomos.sensorplatform.tools;

/**
 * Controller class for gatttool command line tool. You may use multiple instances of it at once.<br />
 * Gatttool is an interactive(!) tool for Bluetooth Low Energy funtions.
 *
 * @author Daniel Pyka
 *
 */
public interface Gatttool {

  /**
   * Open an instance of gatttool in the bash process and connect to a sensor.
   */
  public void setup();

  public void enableLogging();

  public void disableLogging();

  /**
   * Disconnect from the sensor, exit gatttool and shell gracefully.
   */
  public void closeGracefully();

}
