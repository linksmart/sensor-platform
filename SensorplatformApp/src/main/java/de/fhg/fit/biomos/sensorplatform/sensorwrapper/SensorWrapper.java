package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.sensor.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface SensorWrapper<T extends Sensor<?>> {

  public T getSensor();

  public Gatttool getGatttool();

  public void enableLogging();

  public void disableLogging();

}
