package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHrmWrapper extends AbstractHeartRateSensorWrapper {

  // private static final Logger LOG = LoggerFactory.getLogger(TomTomHrmWrapper.class);

  private final TomTomHRM tomtomhrm;

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, HeartRateSampleCollector hrsCollector) {
    super(tomtomhrm.getAddressType(), tomtomhrm.getBDaddress(), hrsCollector);
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
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.tomtomhrm.calculateHeartRateSample(handle, rawHexValues);

    if (this.hrsCollector != null) {
      this.hrsCollector.addToQueue(hrs);
    }
  }

  @Override
  public String getBDaddress() {
    return this.tomtomhrm.getBDaddress();
  }

  @Override
  public SensorName getDeviceName() {
    return this.tomtomhrm.getName();
  }

  @Override
  public String toString() {
    return this.tomtomhrm.getBDaddress() + " " + this.tomtomhrm.getName();
  }

}
