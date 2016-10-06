package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;

/**
 * A new instance of SensorOverseer is used for every recording period. This will check when a SensorWrapper received the last notification. If a certain time
 * frame is exceeded, the sensor is considered offline but the Gatttool is still up and running. The SensorOverseer will handle reconnection attempts until it
 * is reconnected or the recording period is finished. This checking is running in a separate thread during recording.
 *
 * @author Daniel Pyka
 *
 */
public class SensorOverseer implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SensorOverseer.class);

  private final List<AbstractSensorWrapper<?>> wrapperWithLostSensor = new ArrayList<AbstractSensorWrapper<?>>();

  private final List<AbstractSensorWrapper<?>> swList;

  private final int noNotificationTriggerTime;

  private final HardwarePlatform hwPlatform;

  /**
   *
   * @param hwPlatform
   *          provided by Controller and Guice
   * @param noNotificationTriggerTime
   *          time in seconds
   * @param swList
   *          the AbstractSensorWrapper of the current recording period
   */
  public SensorOverseer(HardwarePlatform hwPlatform, int noNotificationTriggerTime, List<AbstractSensorWrapper<?>> swList) {
    this.hwPlatform = hwPlatform;
    this.noNotificationTriggerTime = noNotificationTriggerTime;
    this.swList = swList;
  }

  /**
   * Background thread during recording.
   */
  @Override
  public void run() {
    LOG.info("start supervising");
    while (!Thread.currentThread().isInterrupted()) {
      long currentTime = System.currentTimeMillis();

      for (AbstractSensorWrapper<?> asw : this.swList) {
        if ((currentTime - asw.getLastNotifactionTimestamp()) > (this.noNotificationTriggerTime * 1000)) {
          if (!this.wrapperWithLostSensor.contains(asw)) {
            LOG.warn(asw.getSensor().toString() + " did not send a notification within " + this.noNotificationTriggerTime + "s");
            this.wrapperWithLostSensor.add(asw);
            asw.getGatttool().reconnect();
            LOG.info("attempt to reconnect");
            this.hwPlatform.setLEDstateERROR();
          }
        }
      }

      for (Iterator<AbstractSensorWrapper<?>> iterator = this.wrapperWithLostSensor.iterator(); iterator.hasNext();) {
        AbstractSensorWrapper<?> asw = iterator.next();
        switch (asw.getGatttool().getInternalState()) {
          case RECONNECTING:
            break;
          case DISCONNECTED:
            asw.getGatttool().reconnect();
            LOG.info(asw.getSensor().toString() + " (still) not connected");
            break;
          case CONNECTED:
            asw.enableLogging();
            iterator.remove();
            LOG.info(asw.getSensor().toString() + " reconnected successfully");
            this.hwPlatform.setLEDstateRECORDING();
            break;
        }
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        LOG.info("interrupt received from Controller");
        Thread.currentThread().interrupt();
      }
    }
    this.wrapperWithLostSensor.clear();
    LOG.info("observer thread finished");
  }

}
