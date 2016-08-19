package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

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

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, String timeStampFormat, HeartRateSampleCollector hrsCollector) {
    super(tomtomhrm.getAddressType(), tomtomhrm.getBDaddress(), timeStampFormat, hrsCollector);
    this.tomtomhrm = tomtomhrm;
  }

  @Override
  public void enableLogging() {
    this.tomtomhrm.enableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.tomtomhrm.disableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.tomtomhrm.calculateHeartRateSample(this.dtf.print(new DateTime()), handle, rawHexValues);

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
