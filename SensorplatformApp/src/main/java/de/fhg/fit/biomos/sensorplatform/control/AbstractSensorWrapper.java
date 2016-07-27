package de.fhg.fit.biomos.sensorplatform.control;

import de.fhg.fit.biomos.sensorplatform.persistence.TextFileLogger;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

public abstract class AbstractSensorWrapper implements SensorWrapper, SensorNotificationDataObserver {

  // private static final Logger LOG = LoggerFactory.getLogger(AbstractSensorWrapper.class);

  protected final Uploader uploader;
  protected final Gatttool gatttool;
  protected final TextFileLogger sampleLogger;

  protected long lastNotificationTimestamp;

  public AbstractSensorWrapper(Sensor sensor, Uploader uploader) {
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(sensor.getAddressType(), sensor.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.sampleLogger = new TextFileLogger(sensor.getName().name());

    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public boolean connectToSensor(int timeout) {
    if (this.gatttool.connect(timeout)) {
      // just "refreshing" to not confuse the SensorObserver
      this.lastNotificationTimestamp = System.currentTimeMillis();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean reconnectToSensor(int timeout) {
    if (this.gatttool.reconnect(timeout)) {
      // just "refreshing" to not confuse the SensorObserver
      this.lastNotificationTimestamp = System.currentTimeMillis();
      return true;
    } else {
      return false;
    }
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
    this.sampleLogger.close();
  }

  @Override
  public long getLastNotifactionTimestamp() {
    return this.lastNotificationTimestamp;
  }

}
