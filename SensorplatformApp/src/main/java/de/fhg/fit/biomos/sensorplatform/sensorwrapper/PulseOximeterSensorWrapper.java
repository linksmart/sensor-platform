package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.PulseOximeterSampleCollector;
import de.fhg.fit.biomos.sensorplatform.control.SampleCollector;
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

  // private static final Logger LOG = LoggerFactory.getLogger(PulseOximeterSensorWrapper.class);

  protected final PulseOximeterSampleCollector pulseOximeterSampleCollector;

  public PulseOximeterSensorWrapper(AbstractPulseOximeterSensor sensor, String timestampFormat, String firstname, String lastname,
      PulseOximeterSampleCollector pulseOximeterSampleCollector) {
    super(sensor, timestampFormat, firstname, lastname);
    this.pulseOximeterSampleCollector = pulseOximeterSampleCollector;
  }

  @Override
  public SampleCollector getSampleCollector() {
    return this.pulseOximeterSampleCollector;
  }

  @Override
  public void newNotificationData(Gatttool gatttool, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    PulseOximeterSample pos = this.sensor.calculatePulseOximeterData(this.dtf.print(new DateTime()), handle, rawHexValues);
    if (pos != null) {
      pos.setFirstname(this.firstname);
      pos.setLastname(this.lastname);
      // LOG.info(pos.toString());
      this.pulseOximeterSampleCollector.addToQueue(pos);
    }
  }
}
