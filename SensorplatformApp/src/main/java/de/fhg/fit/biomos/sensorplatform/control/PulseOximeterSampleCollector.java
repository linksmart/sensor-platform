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

  private static final int SLEEP_TIME_MS = 200;

  private final DBcontroller dbc;

  private final Queue<PulseOximeterSample> queue = new LinkedList<PulseOximeterSample>();

  private boolean start;

  @Inject
  public PulseOximeterSampleCollector(DBcontroller dbc) {
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
      if (!this.queue.isEmpty()) {
        storeSample(this.queue.poll());
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
