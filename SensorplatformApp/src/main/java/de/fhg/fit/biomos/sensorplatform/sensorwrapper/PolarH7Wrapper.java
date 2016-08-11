package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7Wrapper extends AbstractSensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7Wrapper.class);

  private final PolarH7 polarh7;

  public PolarH7Wrapper(PolarH7 polarh7, HeartRateSampleCollector hrsCollector) {
    super(polarh7, hrsCollector);
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
    LOG.info("new notification arrived");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.polarh7.calculateHeartRateData(handle, rawHexValues);

    // System.out.println(sample.toString()); // extreme debugging

    if (this.sampleCollector != null) {
      this.sampleCollector.addToQueue(hrs);
    }
  }

  @Override
  public String toString() {
    return this.polarh7.getBdaddress() + " " + this.polarh7.getName();
  }

}