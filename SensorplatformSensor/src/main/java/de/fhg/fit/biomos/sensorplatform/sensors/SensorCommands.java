package de.fhg.fit.biomos.sensorplatform.sensors;

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
   */
  void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification);

  /**
   * Disable the measurement and notification of the sensor previously enabled.
   */
  void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification);

}
