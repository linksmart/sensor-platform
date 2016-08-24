package de.fhg.fit.biomos.sensorplatform.control;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650SampleCollector implements SampleCollector {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650SampleCollector.class);

  private static final int SLEEP_TIME_MS = 200;

  private final DBcontroller dbc;

  private final Queue<CC2650TemperatureSample> queueTemp = new LinkedList<CC2650TemperatureSample>();
  private final Queue<CC2650HumiditySample> queueHum = new LinkedList<CC2650HumiditySample>();
  private final Queue<CC2650PressureSample> queuePress = new LinkedList<CC2650PressureSample>();
  private final Queue<CC2650AmbientlightSample> queueAmb = new LinkedList<CC2650AmbientlightSample>();
  private final Queue<CC2650MovementSample> queueMov = new LinkedList<CC2650MovementSample>();

  private boolean start;

  @Inject
  public CC2650SampleCollector(DBcontroller dbc) {
    this.dbc = dbc;
    this.start = false;
  }

  @Override
  public boolean getStartFlag() {
    return this.start;
  }

  @Override
  public void setStartFlag(boolean start) {
    this.start = start;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      if (!this.queueTemp.isEmpty()) {
        storeSample(this.queueTemp.poll());
      }
      if (!this.queueHum.isEmpty()) {
        storeSample(this.queueHum.poll());
      }
      if (!this.queuePress.isEmpty()) {
        storeSample(this.queuePress.poll());
      }
      if (!this.queueAmb.isEmpty()) {
        storeSample(this.queueAmb.poll());
      }
      if (!this.queueMov.isEmpty()) {
        storeSample(this.queueMov.poll());
      }
      try {
        Thread.sleep(SLEEP_TIME_MS);
      } catch (InterruptedException e) {
        LOG.info("interrupt received from Controller");
        Thread.currentThread().interrupt();
      }
    }
    this.start = false;
    LOG.info("thread finished");
  }

  public void addToQueue(CC2650TemperatureSample sample) {
    this.queueTemp.add(sample);
  }

  public void addToQueue(CC2650HumiditySample sample) {
    this.queueHum.add(sample);
  }

  public void addToQueue(CC2650PressureSample sample) {
    this.queuePress.add(sample);
  }

  public void addToQueue(CC2650AmbientlightSample sample) {
    this.queueAmb.add(sample);
  }

  public void addToQueue(CC2650MovementSample sample) {
    this.queueMov.add(sample);
  }

  private void storeSample(CC2650TemperatureSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650TemperatureSample(sample);
    dbs.commit();
    dbs.close();
  }

  private void storeSample(CC2650HumiditySample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650HumiditySample(sample);
    dbs.commit();
    dbs.close();
  }

  private void storeSample(CC2650PressureSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650PressureAample(sample);
    dbs.commit();
    dbs.close();
  }

  private void storeSample(CC2650AmbientlightSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650AmbientlightSample(sample);
    dbs.commit();
    dbs.close();
  }

  private void storeSample(CC2650MovementSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650MovementSample(sample);
    dbs.commit();
    dbs.close();
  }

}
