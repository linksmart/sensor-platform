package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.SampleCollector;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool.State;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractSensorWrapper implements SensorWrapper, SensorNotificationDataObserver {

  protected final SampleCollector sampleCollector;
  protected final Gatttool gatttool;

  protected long lastNotificationTimestamp;

  public AbstractSensorWrapper(Sensor sensor, SampleCollector sampleCollector) {
    this.sampleCollector = sampleCollector;

    this.gatttool = new GatttoolImpl(sensor.getAddressType(), sensor.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public State getGatttoolInternalState() {
    return this.gatttool.getInternalState();
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
