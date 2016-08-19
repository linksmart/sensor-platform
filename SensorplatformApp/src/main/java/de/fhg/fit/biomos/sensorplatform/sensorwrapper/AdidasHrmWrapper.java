package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.AdidasMiCoachHRM;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 * @author Daniel Pyka
 *
 */
public class AdidasHrmWrapper extends AbstractHeartRateSensorWrapper {

  // private static final Logger LOG = LoggerFactory.getLogger(AdidasHrmWrapper.class);

  private final AdidasMiCoachHRM adidasHrm;

  public AdidasHrmWrapper(AdidasMiCoachHRM adidasHrm, String timeStampFormat, HeartRateSampleCollector hrsCollector) {
    super(adidasHrm.getAddressType(), adidasHrm.getBDaddress(), timeStampFormat, hrsCollector);
    this.adidasHrm = adidasHrm;
  }

  @Override
  public void enableLogging() {
    this.adidasHrm.enableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.adidasHrm.disableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    HeartRateSample hrs = this.adidasHrm.calculateHeartRateSample(this.dtf.print(new DateTime()), handle, rawHexValues);

    if (this.hrsCollector != null) {
      this.hrsCollector.addToQueue(hrs);
    }
  }

  @Override
  public String getBDaddress() {
    return this.adidasHrm.getBDaddress();
  }

  @Override
  public SensorName getDeviceName() {
    return this.adidasHrm.getName();
  }

  @Override
  public String toString() {
    return this.adidasHrm.getBDaddress() + " " + this.adidasHrm.getName();
  }

}
