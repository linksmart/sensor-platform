package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface SensorWrapper {

  public Sensor getSensor();

  public Gatttool getGatttool();

  public void enableLogging();

  public void disableLogging();

}
