package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensors.PolarH7;

/**
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7Wrapper extends AbstractHeartRateSensorWrapper<PolarH7> {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7Wrapper.class);

  public PolarH7Wrapper(PolarH7 sensor, String timeStampFormat, HeartRateSampleCollector hrsCollector) {
    super(sensor, timeStampFormat, hrsCollector);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    if (handle.equals(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT)) {
      HeartRateSample hrs = this.sensor.calculateHeartRateData(this.dtf.print(new DateTime()), rawHexValues);
      this.hrsCollector.addToQueue(hrs);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
    }
  }

}
