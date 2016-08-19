package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;

/**
 *
 * @author Daniel Pyka
 *
 */
public class SensorObserver implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SensorObserver.class);

  private final List<AbstractSensorWrapper> wrapperWithLostSensor = new ArrayList<AbstractSensorWrapper>();

  private final List<AbstractSensorWrapper> swList;

  private final int noNotificationTriggerTime;

  public SensorObserver(int noNotificationTriggerTime, List<AbstractSensorWrapper> swList) {
    this.noNotificationTriggerTime = noNotificationTriggerTime;
    this.swList = swList;
  }

  @Override
  public void run() {
    LOG.info("start observing");
    while (!Thread.currentThread().isInterrupted()) {
      long currentTime = System.currentTimeMillis();
      for (AbstractSensorWrapper sw : this.swList) {
        if ((currentTime - sw.getLastNotifactionTimestamp()) > (this.noNotificationTriggerTime * 1000)) {
          if (!this.wrapperWithLostSensor.contains(sw)) {
            this.wrapperWithLostSensor.add(sw);
            LOG.warn(sw.toString() + "did not send a notification within " + this.noNotificationTriggerTime + "s");
            sw.disconnectBlocking();
            try {
              Thread.sleep(2000); // give bluez time to update itself
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
      if (!this.wrapperWithLostSensor.isEmpty()) {
        for (Iterator<AbstractSensorWrapper> iterator = this.wrapperWithLostSensor.iterator(); iterator.hasNext();) {
          AbstractSensorWrapper sw = iterator.next();
          switch (sw.getGatttoolInternalState()) {
            case RECONNECTING:
              break;
            case DISCONNECTED:
              sw.reconnectToSensor();
              LOG.info(sw.toString() + " (still) not connected");
              break;
            case CONNECTED:
              sw.enableLogging();
              iterator.remove();
              LOG.info(sw.toString() + " reconnected successfully");
              break;
          }
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
