package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7Wrapper extends AbstractHeartRateSensorWrapper {

  // private static final Logger LOG = LoggerFactory.getLogger(PolarH7Wrapper.class);

  private final PolarH7 polarh7;

  public PolarH7Wrapper(PolarH7 polarh7, HeartRateSampleCollector hrsCollector) {
    super(polarh7.getAddressType(), polarh7.getBDaddress(), hrsCollector);
    this.polarh7 = polarh7;
  }

  @Override
  public void enableLogging() {
    this.polarh7.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.polarh7.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.polarh7.calculateHeartRateSample(handle, rawHexValues);

    if (this.hrsCollector != null && hrs != null) {
      this.hrsCollector.addToQueue(hrs);
    }
  }

  @Override
  public String getBDaddress() {
    return this.polarh7.getBDaddress();
  }

  @Override
  public SensorName getDeviceName() {
    return this.polarh7.getName();
  }

  @Override
  public String toString() {
    return this.polarh7.getBDaddress() + " " + this.polarh7.getName();
  }

}
