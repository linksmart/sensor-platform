package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.control.CC2650SampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

/**
 * SensorWrapper specifically used for the CC2650 sensor and CC2650SampleCollector.
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Wrapper extends AbstractSensorWrapper<CC2650> {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650SampleCollector cc2650Collector;

  public CC2650Wrapper(CC2650 cc2650, String timeStampFormat, CC2650SampleCollector cc2650Collector) {
    super(cc2650, timeStampFormat);
    this.cc2650Collector = cc2650Collector;
  }

  @Override
  public void newNotificationData(Gatttool gatttool, String handle, String rawHexValues) {
    this.lastNotificationTimestamp = System.currentTimeMillis();
    String data = rawHexValues.replace(" ", "");
    CC2650TemperatureSample temperatureSample;
    if ((temperatureSample = this.sensor.calculateTemperatureData(this.dtf.print(new DateTime()), handle, data)) != null) {
      this.cc2650Collector.addToQueue(temperatureSample);
      return;
    }
    CC2650HumiditySample humiditySample;
    if ((humiditySample = this.sensor.calculateHumidityData(this.dtf.print(new DateTime()), handle, data)) != null) {
      this.cc2650Collector.addToQueue(humiditySample);
      return;
    }
    CC2650PressureSample pressureSample;
    if ((pressureSample = this.sensor.calculatePressureData(this.dtf.print(new DateTime()), handle, data)) != null) {
      this.cc2650Collector.addToQueue(pressureSample);
      return;
    }
    CC2650AmbientlightSample lightSample;
    if ((lightSample = this.sensor.calculateAmbientlightData(this.dtf.print(new DateTime()), handle, data)) != null) {
      this.cc2650Collector.addToQueue(lightSample);
      return;
    }
    CC2650MovementSample movementSample;
    if ((movementSample = this.sensor.calculateMovementSample(this.dtf.print(new DateTime()), handle, data)) != null) {
      this.cc2650Collector.addToQueue(movementSample);
      return;
    }
    LOG.warn("unexpected handle address " + handle + " " + rawHexValues);
  }

}
