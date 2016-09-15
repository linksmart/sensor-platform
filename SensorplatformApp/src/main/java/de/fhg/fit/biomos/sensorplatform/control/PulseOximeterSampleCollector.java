package de.fhg.fit.biomos.sensorplatform.control;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

public class PulseOximeterSampleCollector implements SampleCollector {

  private static final Logger LOG = LoggerFactory.getLogger(PulseOximeterSampleCollector.class);

  private static final int SLEEP_TIME_MS = 300;

  private final DBcontroller dbc;

  private final Queue<PulseOximeterSample> queue = new LinkedList<PulseOximeterSample>();

  private boolean used;

  @Inject
  public PulseOximeterSampleCollector(DBcontroller dbc) {
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

  @Override
  public void run() {
    while (this.used) {
      if (!this.queue.isEmpty()) {
        storeSample(this.queue.poll());
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

  public void addToQueue(PulseOximeterSample sample) {
    this.queue.add(sample);
  }

  private void storeSample(PulseOximeterSample sample) {
    DBsession dbs = this.dbc.getSession();
    dbs.savePulseOximeterSample(sample);
    dbs.commit();
    dbs.close();
  }

}
