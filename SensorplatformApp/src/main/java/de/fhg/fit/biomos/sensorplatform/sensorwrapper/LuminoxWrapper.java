package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.LuminoxSampleCollector;
import de.fhg.fit.biomos.sensorplatform.control.LuminoxSampleCollector;
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

    private final LuminoxSampleCollector luminoxCollector;

    public LuminoxWrapper(Luminox luminox, String timeStampFormat, String firstname, String lastname, LuminoxSampleCollector luminoxCollector) {
        super(luminox, timeStampFormat, firstname, lastname);
        this.luminoxCollector = luminoxCollector;
    }

    @Override
    public SampleCollector getSampleCollector() {
        return this.luminoxCollector;
    }

    @Override
    public void newNotificationData(Gatttool gatttool, String handle, String rawHexValues) {

        this.lastNotificationTimestamp = System.currentTimeMillis();
        String data = rawHexValues.replace(" ", "");
        LuminoxTemperatureSample temperatureSample;
        if ((temperatureSample = this.sensor.calculateTemperatureData(this.lastNotificationTimestamp, handle, data)) != null) {
            temperatureSample.setFirstname(this.firstname);
            temperatureSample.setLastname(this.lastname);
            temperatureSample.setIdExt(this.sensor.getIdExt());
            // LOG.info(temperatureSample.toString());
            this.luminoxCollector.addToQueue(temperatureSample);
        }
        LuminoxOxygenSample oxygenSample;
        if ((oxygenSample = this.sensor.calculateOxygenData(this.lastNotificationTimestamp, handle, data)) != null) {
            oxygenSample.setFirstname(this.firstname);
            oxygenSample.setLastname(this.lastname);
            oxygenSample.setIdExt(this.sensor.getIdExt());
            // LOG.info(humiditySample.toString());
            this.luminoxCollector.addToQueue(oxygenSample);
        }
        LuminoxAirPressureSample pressureSample;
        if ((pressureSample = this.sensor.calculateAirPressureData(this.lastNotificationTimestamp, handle, data)) != null) {
            pressureSample.setFirstname(this.firstname);
            pressureSample.setLastname(this.lastname);
            pressureSample.setIdExt(this.sensor.getIdExt());
            // LOG.info(pressureSample.toString());
            this.luminoxCollector.addToQueue(pressureSample);
        }

        //LOG.warn("unexpected handle address {} {}", handle, rawHexValues);

    }
}
