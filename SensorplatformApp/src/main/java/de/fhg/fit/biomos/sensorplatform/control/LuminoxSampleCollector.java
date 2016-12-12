package de.fhg.fit.biomos.sensorplatform.control;

import com.google.inject.Inject;
import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.*;
import de.fhg.fit.biomos.sensorplatform.sensor.Luminox;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by garagon on 24.11.2016.
 */
public class LuminoxSampleCollector implements SampleCollector {
    private static final Logger LOG = LoggerFactory.getLogger(LuminoxSampleCollector.class);

    private static final int SLEEP_TIME_MS = 300;

    private final DBcontroller dbc;

    private final Queue<LuminoxTemperatureSample> queueTemp = new ConcurrentLinkedQueue<LuminoxTemperatureSample>();
    private final Queue<LuminoxOxygenSample> queueOxy = new ConcurrentLinkedQueue<LuminoxOxygenSample>();
    private final Queue<LuminoxAirPressureSample> queuePress = new ConcurrentLinkedQueue<LuminoxAirPressureSample>();



    private boolean used;

    @Inject
    public LuminoxSampleCollector(DBcontroller dbc) {
        this.dbc = dbc;
        this.used = false;
    }

    @Override
    public boolean isUsed() {
        return this.used;
    }

    @Override
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Regularly check if there are samples in queue. If so, save them in the database.
     */
    @Override
    public void run() {
        while (this.used) {
            if (!this.queueTemp.isEmpty()) {
                LuminoxTemperatureSample tempSample = this.queueTemp.poll();
                System.out.println(tempSample.toStringLinkSmart());
                //LOG.info(tempSample.toStringLinkSmart());
                storeSample(tempSample);
            } else if (!this.queueOxy.isEmpty()) {
                LuminoxOxygenSample oxySample = this.queueOxy.poll();
                System.out.println(oxySample.toStringLinkSmart());
                //LOG.info(oxySample.toStringLinkSmart());
                storeSample(oxySample);
            } else if (!this.queuePress.isEmpty()) {
                LuminoxAirPressureSample pressureSample = this.queuePress.poll();
                System.out.println(pressureSample.toStringLinkSmart());
                //LOG.info(pressureSample.toStringLinkSmart());
                storeSample(pressureSample);
            } else {
                try {
                    Thread.sleep(SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    LOG.info("interrupt received from Controller");
                    Thread.currentThread().interrupt();
                }
            }
        }
        LOG.info("thread finished");
    }

    /**
     * Add a temperature sample to the queue for storing it in the database.
     *
     * @param sample
     *          a CC2650TemperatureSample retrieved from the sensor
     */
    public void addToQueue(LuminoxTemperatureSample sample) {
        this.queueTemp.add(sample);
    }

    /**
     * Add a humidity sample to the queue for storing it in the database.
     *
     * @param sample
     *          a CC2650HumiditySample retrieved from the sensor
     */
    public void addToQueue(LuminoxOxygenSample sample) {
        this.queueOxy.add(sample);
    }

    /**
     * Add a pressure sample to the queue for storing it in the database.
     *
     * @param sample
     *          a CC2650PressureSample retrieved from the sensor
     */
    public void addToQueue(LuminoxAirPressureSample sample) {
        this.queuePress.add(sample);
    }


    /**
     * Save a temperature sample to the database.
     *
     * @param sample
     *          a CC2650TemperatureSample from the queue
     */
    private void storeSample(LuminoxTemperatureSample sample) {
        DBsession dbs = this.dbc.getSession();
        dbs.saveLuminoxTemperatureSample(sample);
        dbs.commit();
        dbs.close();
    }

    /**
     * Save a humidity sample to the database.
     *
     * @param sample
     *          a CC2650HumiditySample from the queue
     */
    private void storeSample(LuminoxOxygenSample sample) {
        DBsession dbs = this.dbc.getSession();
        dbs.saveLuminoxOxygenSample(sample);
        dbs.commit();
        dbs.close();
    }

    /**
     * Save a pressure sample to the database.
     *
     * @param sample
     *          a CC2650PressureSample from the queue
     */
    private void storeSample(LuminoxAirPressureSample sample) {
        DBsession dbs = this.dbc.getSession();
        dbs.saveLuminoxAirPressureSample(sample);
        dbs.commit();
        dbs.close();
    }



}