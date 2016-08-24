package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.PulseOximeterSampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.BLE113lib;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;
import de.fhg.fit.biomos.sensorplatform.sensor.BLE113;

/**
 *
 * @author Daniel Pyka
 *
 */
public class BLE113SensorWrapper extends AbstractPulseOximeterSensorWrapper<BLE113> {

  private static final Logger LOG = LoggerFactory.getLogger(BLE113SensorWrapper.class);

  public BLE113SensorWrapper(BLE113 ble113, String timestampFormat, PulseOximeterSampleCollector pulseOximeterSampleCollector) {
    super(ble113, timestampFormat, pulseOximeterSampleCollector);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // LOG.info("new notification received");
    this.lastNotificationTimestamp = System.currentTimeMillis();

    if (handle.equals(BLE113lib.HANDLE_PULSE_OXIMETER_MEASUREMENT)) {
      PulseOximeterSample pulseOximeterSample = this.sensor.calculatePulseOximeterData(this.dtf.print(new DateTime()), rawHexValues);
      if (this.pulseOximeterSampleCollector != null && pulseOximeterSample != null) {
        this.pulseOximeterSampleCollector.addToQueue(pulseOximeterSample);
      }
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
    }
  }

}
