package de.fhg.fit.biomos.sensorplatform.sensors;

/**
 * Abstraction for all different types of sensors.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorCommands {

  void enableNotification();

  void disableNotification();

  void processSensorData(String handle, String rawHexValues);

}
