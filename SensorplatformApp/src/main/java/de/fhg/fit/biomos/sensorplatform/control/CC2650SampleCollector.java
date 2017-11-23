package de.fhg.fit.biomos.sensorplatform.control;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
 * CC2650SampleCollector stores different types of CC2650 Samples to the database.<br>
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class CC2650SampleCollector implements SampleCollector {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650SampleCollector.class);

  private static final int SLEEP_TIME_MS = 300;

  private final DBcontroller dbc;

  private final Queue<CC2650TemperatureSample> queueTemp = new ConcurrentLinkedQueue<CC2650TemperatureSample>();
  private final Queue<CC2650HumiditySample> queueHum = new ConcurrentLinkedQueue<CC2650HumiditySample>();
  private final Queue<CC2650PressureSample> queuePress = new ConcurrentLinkedQueue<CC2650PressureSample>();
  private final Queue<CC2650AmbientlightSample> queueAmb = new ConcurrentLinkedQueue<CC2650AmbientlightSample>();
  private final Queue<CC2650MovementSample> queueMov = new ConcurrentLinkedQueue<CC2650MovementSample>();

  private boolean used;

  @Inject
  public CC2650SampleCollector(DBcontroller dbc) {
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
        storeSample(this.queueTemp.poll());
      } else if (!this.queueHum.isEmpty()) {
        storeSample(this.queueHum.poll());
      } else if (!this.queuePress.isEmpty()) {
        storeSample(this.queuePress.poll());
      } else if (!this.queueAmb.isEmpty()) {
        storeSample(this.queueAmb.poll());
      } else if (!this.queueMov.isEmpty()) {
        storeSample(this.queueMov.poll());
      } else {
        try {
          Thread.sleep(SLEEP_TIME_MS);
        } catch (InterruptedException e) {
          LOG.info("interrupt received from Controller CC2650");
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
  public void addToQueue(CC2650TemperatureSample sample) {
    this.queueTemp.add(sample);
  }

  /**
   * Add a humidity sample to the queue for storing it in the database.
   *
   * @param sample
   *          a CC2650HumiditySample retrieved from the sensor
   */
  public void addToQueue(CC2650HumiditySample sample) {
    this.queueHum.add(sample);
  }

  /**
   * Add a pressure sample to the queue for storing it in the database.
   *
   * @param sample
   *          a CC2650PressureSample retrieved from the sensor
   */
  public void addToQueue(CC2650PressureSample sample) {
    this.queuePress.add(sample);
  }

  /**
   * Add an ambientlight sample to the queue for storing it in the database.
   *
   * @param sample
   *          a CC2650AmbientlightSample retrieved from the sensor
   */
  public void addToQueue(CC2650AmbientlightSample sample) {
    this.queueAmb.add(sample);
  }

  /**
   * Add a movement sample to the queue for storing it in the database.
   *
   * @param sample
   *          a CC2650MovementSample retrieved from the sensor
   */
  public void addToQueue(CC2650MovementSample sample) {
    this.queueMov.add(sample);
  }

  /**
   * Save a temperature sample to the database.
   *
   * @param sample
   *          a CC2650TemperatureSample from the queue
   */
  private void storeSample(CC2650TemperatureSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650TemperatureSample(sample);
    dbs.commit();
    dbs.close();
  }

  /**
   * Save a humidity sample to the database.
   *
   * @param sample
   *          a CC2650HumiditySample from the queue
   */
  private void storeSample(CC2650HumiditySample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650HumiditySample(sample);
    dbs.commit();
    dbs.close();
  }

  /**
   * Save a pressure sample to the database.
   *
   * @param sample
   *          a CC2650PressureSample from the queue
   */
  private void storeSample(CC2650PressureSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650PressureSample(sample);
    dbs.commit();
    dbs.close();
  }

  /**
   * Save an ambientlight sample to the database.
   *
   * @param sample
   *          a CC2650AmbientlightSample from the queue
   */
  private void storeSample(CC2650AmbientlightSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650AmbientlightSample(sample);
    dbs.commit();
    dbs.close();
  }

  /**
   * Save a movement sample to the database.
   *
   * @param sample
   *          a CC2650MovementSample from the queue
   */
  private void storeSample(CC2650MovementSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveCC2650MovementSample(sample);
    dbs.commit();
    dbs.close();
  }

}
