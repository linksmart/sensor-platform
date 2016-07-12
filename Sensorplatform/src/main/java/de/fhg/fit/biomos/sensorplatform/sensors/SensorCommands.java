package de.fhg.fit.biomos.sensorplatform.sensors;

/**
 * Defines methods, which are called from instances of GatttoolImpl.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorCommands {

  /**
   * Enable the measurement of the sensor as defined in the sensor configuration of the sensor.
   */
  void enableNotification();

  /**
   * Disable the measurement of the sensor previously enabled.
   */
  void disableNotification();

  /**
   * Calculate the (readable) value of the given hexadecimal input string and print it to a file. If specified in the sensor configuration, the data is also
   * sent to a web interface.
   *
   * @param handle
   *          specific for a measure (like temperature or heart rate)
   * @param rawHexValues
   *          raw values the sensorplatform receives
   */
  void processSensorData(String handle, String rawHexValues);

}
