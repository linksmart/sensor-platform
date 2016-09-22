package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.AbstractHeartRateSensor;

/**
 * SensorWrapper for all heart rate sensors which are compliant with the Bluetooth specification.
 *
 * @author Daniel Pyka
 *
 */
public class HeartRateSensorWrapper extends AbstractSensorWrapper<AbstractHeartRateSensor> {

  protected final HeartRateSampleCollector hrsCollector;

  public HeartRateSensorWrapper(AbstractHeartRateSensor sensor, String timestampFormat, HeartRateSampleCollector hrsCollector) {
    super(sensor, timestampFormat);
    this.hrsCollector = hrsCollector;
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.sensor.calculateHeartRateData(this.dtf.print(new DateTime()), handle, rawHexValues);
    if (hrs != null) {
      this.hrsCollector.addToQueue(hrs);
    }
  }

}
