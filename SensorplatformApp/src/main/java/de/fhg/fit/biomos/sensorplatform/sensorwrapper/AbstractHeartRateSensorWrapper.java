package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sensors.AbstractHeartRateSensor;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractHeartRateSensorWrapper<T extends AbstractHeartRateSensor> extends AbstractSensorWrapper<AbstractHeartRateSensor> {

  protected final HeartRateSampleCollector hrsCollector;

  public AbstractHeartRateSensorWrapper(T sensor, String timestampFormat, HeartRateSampleCollector hrsCollector) {
    super(sensor, timestampFormat);
    this.hrsCollector = hrsCollector;
  }

}
