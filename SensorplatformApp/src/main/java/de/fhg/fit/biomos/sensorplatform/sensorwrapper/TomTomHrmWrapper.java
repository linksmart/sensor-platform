package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.TomTomHRMlib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;

/**
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHrmWrapper extends AbstractHeartRateSensorWrapper<TomTomHRM> {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomHrmWrapper.class);

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, String timeStampFormat, HeartRateSampleCollector hrsCollector) {
    super(tomtomhrm, timeStampFormat, hrsCollector);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    if (handle.equals(TomTomHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      HeartRateSample hrs = this.sensor.calculateHeartRateData(this.dtf.print(new DateTime()), rawHexValues);
      this.hrsCollector.addToQueue(hrs);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
    }

  }

}
