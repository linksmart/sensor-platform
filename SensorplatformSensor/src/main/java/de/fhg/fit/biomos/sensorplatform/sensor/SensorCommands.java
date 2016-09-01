package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;

/**
 * Defines methods, which are called from the corresponding SensorWrapper.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorCommands {

  /**
   * Enable the measurement and notification of the sensor as defined in the settings of the sensor.
   *
   * @param streamToSensor
   *          gatttool input stream
   * @param charWriteCmd
   *          gatttool write command
   * @param enableNotification
   *          as defined in the bluetooth specification (01:00)
   */
  public void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification);

  /**
   * Disable the measurement and notification of the sensor previously enabled.
   *
   * @param streamToSensor
   *          gatttool input stream
   * @param charWriteCmd
   *          gatttool write command
   * @param disableNotification
   *          as defined in the bluetooth specification (00:00)
   */
  public void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification);

}
