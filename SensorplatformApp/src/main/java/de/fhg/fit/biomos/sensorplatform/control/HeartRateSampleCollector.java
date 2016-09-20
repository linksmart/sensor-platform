package de.fhg.fit.biomos.sensorplatform.control;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Nullable;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 *
 * @author Daniel Pyka
 *
 */
public class HeartRateSampleCollector implements SampleCollector {

  private static final Logger LOG = LoggerFactory.getLogger(HeartRateSampleCollector.class);

  private static final int UPLOAD_THREAD_SLEEP_TIME_MS = 300;
  private static final int UPLOAD_ATTEMPTS = 1;

  private final DBcontroller dbc;
  private final Uploader uploader;

  private final Queue<HeartRateSample> queue = new LinkedList<HeartRateSample>();

  private boolean used;

  @Inject
  public HeartRateSampleCollector(DBcontroller dbc, @Nullable Uploader uploader) {
    this.dbc = dbc;
    this.used = false;
    this.uploader = uploader;
  }

  @Override
  public boolean isUsed() {
    return this.used;
  }

  @Override
  public void setUsed(boolean used) {
    this.used = used;
  }

  public void addToQueue(HeartRateSample hrs) {
    this.queue.add(hrs);
  }

  public int getNumberOfHrsInQueue() {
    return this.queue.size();
  }

  private void uploadSample(HeartRateSample hrs) {
    int attempt = 1;
    while (attempt <= UPLOAD_ATTEMPTS) {
      try {
        int statusCode = this.uploader.sendHeartRateSample(hrs);
        switch (statusCode) {
          case HttpStatus.SC_CREATED:
            hrs.setTransmitted(true);
            // LOG.info("sample transmission successful");
            return;
          case HttpStatus.SC_OK:
            hrs.setTransmitted(true);
            // LOG.info("sample transmission successful");
            return;
          case HttpStatus.SC_UNAUTHORIZED:
            LOG.error("transmission unauthorized - attempting to log in again");
            this.uploader.login();
            break;
          default:
            LOG.error("transmission failed, error code: " + statusCode);
            attempt++;
            break;
        }
      } catch (IOException e) {
        LOG.error("http client execute (sample transmission) failed", e.getMessage());
        attempt++;
      }
    }
  }

  private void storeSample(HeartRateSample hrs) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveHeartRateSample(hrs);
    dbs.commit();
    dbs.close();
  }

  public void run2() {
    if (this.uploader != null) {
      this.uploader.login();
    }
    while (this.used) {

    }
  }

  @Override
  public void run() {
    if (this.uploader != null) {
      this.uploader.login();
    }
    while (this.used) {
      if (!this.queue.isEmpty()) {
        HeartRateSample hrs = this.queue.peek();
        if (this.uploader != null) {
          uploadSample(hrs);
        }
        storeSample(hrs);
        this.queue.poll();
      } else {
        try {
          Thread.sleep(UPLOAD_THREAD_SLEEP_TIME_MS);
        } catch (InterruptedException e) {
          LOG.info("interrupt received from Controller");
        }
      }
    }
    while (!this.queue.isEmpty()) {
      LOG.info("storing all remaining samples");
      LOG.info("queue size: " + this.queue.size());
      storeSample(this.queue.poll());
    }
    LOG.info("thread finished");
  }

}
