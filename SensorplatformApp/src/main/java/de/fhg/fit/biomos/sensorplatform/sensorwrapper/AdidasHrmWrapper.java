package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.AdidasMiCoachHRMlib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.AdidasMiCoachHRM;

/**
 *
 * @author Daniel Pyka
 *
 */
public class AdidasHrmWrapper extends AbstractHeartRateSensorWrapper<AdidasMiCoachHRM> {

  private static final Logger LOG = LoggerFactory.getLogger(AdidasHrmWrapper.class);

  public AdidasHrmWrapper(AdidasMiCoachHRM adidasHrm, String timeStampFormat, HeartRateSampleCollector hrsCollector) {
    super(adidasHrm, timeStampFormat, hrsCollector);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    if (handle.equals(AdidasMiCoachHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      HeartRateSample hrs = this.sensor.calculateHeartRateData(this.dtf.print(new DateTime()), rawHexValues);
      this.hrsCollector.addToQueue(hrs);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
    }
  }

}
