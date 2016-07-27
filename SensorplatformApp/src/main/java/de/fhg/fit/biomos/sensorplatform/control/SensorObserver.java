package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;

/**
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class SensorObserver implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SensorObserver.class);

  private final List<AbstractSensorWrapper> wrapperWithLostSensor = new ArrayList<AbstractSensorWrapper>();
  private List<String> foundDevices = new ArrayList<String>();

  private List<AbstractSensorWrapper> swList;

  private final Hcitool hcitool = new HcitoolImpl();
  private final int lescanDuration;
  private final int defaultSensorTimeout;
  private final int noNotificationTriggerTime;

  @Inject
  public SensorObserver(@Named("hcitool.lescan.duration") String lescanDuration, @Named("default.sensor.timeout") String defaultSensorTimeout,
      @Named("sensor.no.notification.trigger.time") String noNotificationTriggerTime) {
    this.lescanDuration = new Integer(lescanDuration);
    this.defaultSensorTimeout = new Integer(defaultSensorTimeout);
    this.noNotificationTriggerTime = new Integer(noNotificationTriggerTime);
  }

  public void setTarget(List<AbstractSensorWrapper> swList) {
    this.swList = swList;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      long currentTime = System.currentTimeMillis();
      for (AbstractSensorWrapper sw : this.swList) {
        if ((currentTime - sw.getLastNotifactionTimestamp()) > (this.noNotificationTriggerTime * 1000)) {
          LOG.warn("sensor " + sw.getSensor().getBdaddress() + " did not send a notification within " + this.noNotificationTriggerTime + "s");
          if (!this.wrapperWithLostSensor.contains(sw)) {
            this.wrapperWithLostSensor.add(sw);
            LOG.info("");
          }
        }
      }
      if (!this.wrapperWithLostSensor.isEmpty()) {
        this.foundDevices = this.hcitool.findDevices(this.lescanDuration); // TODO DO: stop scan properly if interrupted by controller after uptime
        tryToReconnectSensors();
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        LOG.info("interrupt received from Controller");
        Thread.currentThread().interrupt();
      }
    }
    LOG.info("observer thread finished");
  }

  private void tryToReconnectSensors() {
    for (Iterator<AbstractSensorWrapper> iterator = this.wrapperWithLostSensor.iterator(); iterator.hasNext();) {
      AbstractSensorWrapper sw = iterator.next();
      for (String bdAddress : this.foundDevices) {
        if (sw.getSensor().getBdaddress().equals(bdAddress)) {
          LOG.info("attempting to reconnect to " + bdAddress);
          if (sw.reconnectToSensor(this.defaultSensorTimeout)) {
            sw.enableLogging();
            iterator.remove();
            LOG.info("Sensor " + bdAddress + " reconnected successfully!");
          }
        }
      }
    }
    // for (AbstractSensorWrapper sw : this.wrapperWithLostSensor) {
    // this.wrapperWithLostSensor.remove(sw);
    // }
  }

}
