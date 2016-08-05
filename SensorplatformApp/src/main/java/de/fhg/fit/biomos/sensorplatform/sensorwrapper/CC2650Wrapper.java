package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.TextFileLogger;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650Sample;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Wrapper extends AbstractSensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650 cc2650;

  private final TextFileLogger sampleLogger;

  public CC2650Wrapper(CC2650 cc2650) {
    super(cc2650, null);
    this.cc2650 = cc2650;
    this.sampleLogger = new TextFileLogger(cc2650.getName().name());
  }

  @Override
  public void enableLogging() {
    this.cc2650.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.cc2650.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    LOG.info("new notification arrived");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    CC2650Sample sample = this.cc2650.calculateSensorData(handle, rawHexValues);

    this.sampleLogger.writeLine(sample.toString());

    // System.out.println(sample.toString()); // extreme debugging
  }

  @Override
  public void shutdown() {
    this.gatttool.exitGatttool();
    this.sampleLogger.close();
  }

  @Override
  public String toString() {
    return this.cc2650.getBdaddress() + " " + this.cc2650.getName();
  }

}
