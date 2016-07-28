package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;

/**
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class SensorObserver implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SensorObserver.class);

  private final List<AbstractSensorWrapper> wrapperWithLostSensor = new ArrayList<AbstractSensorWrapper>();

  private List<AbstractSensorWrapper> swList;

  private final int noNotificationTriggerTime;

  @Inject
  public SensorObserver(@Named("default.sensor.timeout") String defaultSensorTimeout) {
    this.noNotificationTriggerTime = new Integer(defaultSensorTimeout) * 2;
  }

  public void setTarget(List<AbstractSensorWrapper> swList) {
    this.swList = swList;
    LOG.info("set sensorwrapper to observe");
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
              Thread.sleep(2000); // give gatttool time react
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
    this.swList.clear();
    LOG.info("observer thread finished");
  }

}
