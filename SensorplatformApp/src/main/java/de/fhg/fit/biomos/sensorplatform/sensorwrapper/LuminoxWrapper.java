package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.CC2650SampleCollector;
import de.fhg.fit.biomos.sensorplatform.control.SampleCollector;
import de.fhg.fit.biomos.sensorplatform.sample.*;
import de.fhg.fit.biomos.sensorplatform.sensor.Luminox;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by garagon on 24.11.2016.
 */
public class LuminoxWrapper extends AbstractSensorWrapper<Luminox> {
    private static final Logger LOG = LoggerFactory.getLogger(LuminoxWrapper.class);

    private final CC2650SampleCollector cc2650Collector;

    public LuminoxWrapper(Luminox luminox, String timeStampFormat, String firstname, String lastname, CC2650SampleCollector cc2650Collector) {
        super(luminox, timeStampFormat, firstname, lastname);
        this.cc2650Collector = cc2650Collector;
    }

    @Override
    public SampleCollector getSampleCollector() {
        return this.cc2650Collector;
    }

    @Override
    public void newNotificationData(Gatttool gatttool, String handle, String rawHexValues) {
        /*
        this.lastNotificationTimestamp = System.currentTimeMillis();
        String data = rawHexValues.replace(" ", "");
        CC2650TemperatureSample temperatureSample;
        if ((temperatureSample = this.sensor.calculateTemperatureData(this.dtf.print(new DateTime()), handle, data)) != null) {
            temperatureSample.setFirstname(this.firstname);
            temperatureSample.setLastname(this.lastname);
            // LOG.info(temperatureSample.toString());
            this.cc2650Collector.addToQueue(temperatureSample);
            return;
        }
        CC2650HumiditySample humiditySample;
        if ((humiditySample = this.sensor.calculateHumidityData(this.dtf.print(new DateTime()), handle, data)) != null) {
            humiditySample.setFirstname(this.firstname);
            humiditySample.setLastname(this.lastname);
            // LOG.info(humiditySample.toString());
            this.cc2650Collector.addToQueue(humiditySample);
            return;
        }
        CC2650PressureSample pressureSample;
        if ((pressureSample = this.sensor.calculatePressureData(this.dtf.print(new DateTime()), handle, data)) != null) {
            pressureSample.setFirstname(this.firstname);
            pressureSample.setLastname(this.lastname);
            // LOG.info(pressureSample.toString());
            this.cc2650Collector.addToQueue(pressureSample);
            return;
        }
        CC2650AmbientlightSample lightSample;
        if ((lightSample = this.sensor.calculateAmbientlightData(this.dtf.print(new DateTime()), handle, data)) != null) {
            lightSample.setFirstname(this.firstname);
            lightSample.setLastname(this.lastname);
            // LOG.info(lightSample.toString());
            this.cc2650Collector.addToQueue(lightSample);
            return;
        }
        CC2650MovementSample movementSample;
        if ((movementSample = this.sensor.calculateMovementSample(this.dtf.print(new DateTime()), handle, data)) != null) {
            movementSample.setFirstname(this.firstname);
            movementSample.setLastname(this.lastname);
            // LOG.info(movementSample.toString());
            this.cc2650Collector.addToQueue(movementSample);
            return;
        }
        LOG.warn("unexpected handle address {} {}", handle, rawHexValues);
        */
    }
}
