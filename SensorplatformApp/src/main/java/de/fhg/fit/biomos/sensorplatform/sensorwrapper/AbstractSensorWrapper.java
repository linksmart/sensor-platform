package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensor.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool.Mode;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 * Wrapper class for handling Sensor, Gatttool and SampleCollector.
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractSensorWrapper<T extends Sensor<?>> implements SensorWrapper<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractSensorWrapper.class);

  protected final DateTimeFormatter dtf;

  protected final Gatttool gatttool;
  protected final T sensor;

  protected final String firstname;
  protected final String lastname;

  protected long lastNotificationTimestamp;

  protected String requestedHandle;

  /**
   *
   * @param sensor
   *          Sensor which is used by the Wrapper
   * @param timestampFormat
   *          a specified time stamp format which is used for samples
   * @param firstname
   *          the first name of the sensorplatform user
   * @param lastname
   *          the last name of the sensorplatform user
   */
  public AbstractSensorWrapper(T sensor, String timestampFormat, String firstname, String lastname) {
    this.sensor = sensor;
    this.gatttool = new GatttoolImpl(sensor.getBDaddress(), sensor.getAddressType(), sensor.getSecurityLevel());
    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);
    this.firstname = firstname;
    this.lastname = lastname;
    this.lastNotificationTimestamp = 0;
    this.gatttool.setObserver(this);
    new Thread(this.gatttool, this.sensor.getName().name().toLowerCase()).start();
  }

  @Override
  public void enableLogging() {
    this.sensor.enableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
    this.gatttool.setInternalMode(Mode.NOTIFICATION);
  }

  @Override
  public void disableLogging() {
    this.sensor.disableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
    this.gatttool.setInternalMode(Mode.COMMANDMODE);
  }

  @Override
  public T getSensor() {
    return this.sensor;
  }

  @Override
  public Gatttool getGatttool() {
    return this.gatttool;
  }

  @Override
  public long getLastNotifactionTimestamp() {
    return this.lastNotificationTimestamp;
  }

  @Override
  public void getBatteryLevel() {
    this.requestedHandle = this.sensor.requestBatteryLevel(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_READ_HND);
  }

  @Override
  public void newCommandData(Gatttool observable, String rawHexValues) {
    if (this.requestedHandle.equals(this.sensor.getGattLibrary().getHandleBatteryLevel())) {
      LOG.info("Battery level of {}: {}", this.sensor.getName(), this.sensor.calculateBatteryLevel(rawHexValues));
    }
  }

}
