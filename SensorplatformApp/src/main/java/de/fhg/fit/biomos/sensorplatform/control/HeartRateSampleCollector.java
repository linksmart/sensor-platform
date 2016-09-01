package de.fhg.fit.biomos.sensorplatform.control;

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

  private boolean start;

  @Inject
  public HeartRateSampleCollector(DBcontroller dbc, @Nullable Uploader uploader) {
    this.dbc = dbc;
    this.start = false;
    this.uploader = uploader;
  }

  @Override
  public boolean getStartFlag() {
    return this.start;
  }

  @Override
  public void setStartFlag(boolean start) {
    this.start = start;
  }

  public void addToQueue(HeartRateSample hrs) {
    this.queue.add(hrs);
  }

  private void uploadSample(HeartRateSample hrs) {
    int attempt = 1;
    while (attempt <= UPLOAD_ATTEMPTS) {
      int statusCode = this.uploader.sendHeartRateSample(hrs);
      switch (statusCode) {
        case HttpStatus.SC_CREATED:
          hrs.setTransmitted(true);
          // LOG.info("sample transmission successful");
          return;
        case HttpStatus.SC_UNAUTHORIZED:
          LOG.error("transmission unauthorized - attempting to log in again");
          this.uploader.login();
          break;
        case -1:
          // in case of exception, printed to console in Uploader
          attempt++;
          break;
        default:
          LOG.error("transmission failed, error code: " + statusCode);
          attempt++;
          break;
      }
    }
  }

  private void storeSample(HeartRateSample hrs) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveHeartRateSample(hrs);
    dbs.commit();
    dbs.close();
  }

  @Override
  public void run() {
    if (this.uploader != null) {
      this.uploader.login();
    }
    while (!Thread.currentThread().isInterrupted()) {
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
          Thread.currentThread().interrupt();
        }
      }
    }
    while (!this.queue.isEmpty()) {
      LOG.info("storing all remaining samples");
      LOG.info("queue size: " + this.queue.size());
      storeSample(this.queue.poll());
    }
    this.start = false;
    LOG.info("thread finished");
  }

}
