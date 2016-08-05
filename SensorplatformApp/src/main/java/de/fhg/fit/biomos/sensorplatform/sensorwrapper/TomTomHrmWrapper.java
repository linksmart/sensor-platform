package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHrmWrapper extends AbstractSensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomHrmWrapper.class);

  private final TomTomHRM tomtomhrm;

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, HeartRateSampleCollector hrsCollector) {
    super(tomtomhrm, hrsCollector);
    this.tomtomhrm = tomtomhrm;
  }

  @Override
  public void enableLogging() {
    this.tomtomhrm.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.tomtomhrm.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    LOG.info("new notification arrived");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.tomtomhrm.calculateHeartRateData(handle, rawHexValues);

    // System.out.println(sample.toString()); // extreme debugging

    if (this.sampleCollector != null) {
      this.sampleCollector.addToQueue(hrs);
    }
  }

  @Override
  public String toString() {
    return this.tomtomhrm.getBdaddress() + " " + this.tomtomhrm.getName();
  }

}
