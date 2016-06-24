package de.fhg.fit.biomos.sensorplatform.sensors;

/**
 * Abstraction for all different types of sensors.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorCommands {

  void enableLogging();

  void disableLogging();

  void processSensorData(String handle, String data);

}
