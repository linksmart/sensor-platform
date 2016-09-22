package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.fhg.fit.biomos.sensorplatform.sensor.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolCmd;

/**
 * Wrapper class for handling Sensor, Gatttool and SampleCollector.
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractSensorWrapper<T extends Sensor<?>> implements SensorWrapper<T>, SensorNotificationDataObserver {

  protected final DateTimeFormatter dtf;

  protected final Gatttool gatttool;
  protected final T sensor;

  protected long lastNotificationTimestamp;

  /**
   *
   * @param sensor
   *          Sensor which is used by the Wrapper
   * @param timestampFormat
   *          a specified time stamp format which is used for samples
   */
  public AbstractSensorWrapper(T sensor, String timestampFormat) {
    this.sensor = sensor;
    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);
    this.gatttool = new GatttoolImpl(sensor.getBDaddress(), sensor.getAddressType(), sensor.getSecurityLevel());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();
  }

  @Override
  public void enableLogging() {
    this.sensor.enableAllNotification(this.gatttool.getStreamToSensor(), GatttoolCmd.CMD_CHAR_WRITE_CMD, GatttoolCmd.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.sensor.disableAllNotification(this.gatttool.getStreamToSensor(), GatttoolCmd.CMD_CHAR_WRITE_CMD, GatttoolCmd.DISABLE_NOTIFICATION);
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

}
