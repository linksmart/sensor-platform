package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool.State;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolSecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractSensorWrapper implements SensorWrapper, SensorNotificationDataObserver {

  protected final Gatttool gatttool;

  protected final DateTimeFormatter dtf;
  protected long lastNotificationTimestamp;

  public AbstractSensorWrapper(AddressType addressType, String bdAddress, String timestampFormat) {

    this.gatttool = new GatttoolImpl(addressType, bdAddress);
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public State getGatttoolInternalState() {
    return this.gatttool.getInternalState();
  }

  @Override
  public void setSecurityLevel(GatttoolSecurityLevel secLevel) {
    this.gatttool.setSecurityLevel(secLevel);
  }

  @Override
  public boolean connectToSensorBlocking(int timeout) {
    return this.gatttool.connectBlocking(timeout);
  }

  @Override
  public void reconnectToSensor() {
    this.gatttool.reconnect();
  }

  @Override
  public void disconnectBlocking() {
    this.gatttool.disconnectBlocking();
  }

  @Override
  public void disconnect() {
    this.gatttool.disconnect();
  }

  @Override
  public void shutdown() {
    this.gatttool.exitGatttool();
  }

  @Override
  public long getLastNotifactionTimestamp() {
    return this.lastNotificationTimestamp;
  }

}
