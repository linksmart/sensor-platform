package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.PulseOximeterSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sensor.AbstractPulseOximeterSensor;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractPulseOximeterSensorWrapper<T extends AbstractPulseOximeterSensor> extends AbstractSensorWrapper<AbstractPulseOximeterSensor> {

  protected final PulseOximeterSampleCollector pulseOximeterSampleCollector;

  public AbstractPulseOximeterSensorWrapper(T sensor, String timestampFormat, PulseOximeterSampleCollector pulseOximeterSampleCollector) {
    super(sensor, timestampFormat);
    this.pulseOximeterSampleCollector = pulseOximeterSampleCollector;
  }

}
