package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.PulseOximeterSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;
import de.fhg.fit.biomos.sensorplatform.sensor.AbstractPulseOximeterSensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

/**
 * SensorWrapper for all pulse oximeter sensors which are compliant with the Bluetooth specification.
 *
 * @author Daniel Pyka
 *
 */
public class PulseOximeterSensorWrapper extends AbstractSensorWrapper<AbstractPulseOximeterSensor> {

  protected final PulseOximeterSampleCollector pulseOximeterSampleCollector;

  public PulseOximeterSensorWrapper(AbstractPulseOximeterSensor sensor, String timestampFormat, PulseOximeterSampleCollector pulseOximeterSampleCollector) {
    super(sensor, timestampFormat);
    this.pulseOximeterSampleCollector = pulseOximeterSampleCollector;
  }

  @Override
  public void newNotificationData(Gatttool gatttool, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    PulseOximeterSample pulseOximeterSample = this.sensor.calculatePulseOximeterData(this.dtf.print(new DateTime()), handle, rawHexValues);
    if (pulseOximeterSample != null) {
      this.pulseOximeterSampleCollector.addToQueue(pulseOximeterSample);
    }
  }
}
