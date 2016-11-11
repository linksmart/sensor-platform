package de.fhg.fit.biomos.sensorplatform.control;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nullable;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * HeartRateSampleCollector stores heart rate samples to the database and it is able to upload those samples to an external webinterface.<br>
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class HeartRateSampleCollector implements SampleCollector {

  private static final Logger LOG = LoggerFactory.getLogger(HeartRateSampleCollector.class);

  private static final int MAXIMUM_HRS_IN_QUEUE = 100;
  private static final int UPLOAD_THREAD_SLEEP_TIME_MS = 100;

  private final DBcontroller dbc;
  private final HardwarePlatform hwPlatform;
  private final Uploader uploader;

  private final Queue<HeartRateSample> queue = new ConcurrentLinkedQueue<HeartRateSample>();
  private Queue<HeartRateSample> buffer;

  private boolean used;

  @Inject
  public HeartRateSampleCollector(DBcontroller dbc, HardwarePlatform hwPlatform, @Nullable Uploader uploader) {
    this.dbc = dbc;
    this.hwPlatform = hwPlatform;
    this.uploader = uploader;
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
   * Try to upload a sample and store it afterwards in this order. Transmission flag is set accordingly. By using this algorithm, the load of the database is
   * minimised.
   */
  @Override
  public void run() {
    if (this.uploader != null) {
      this.uploader.login();
    }
    while (this.used) {
      if (this.queue.size() > MAXIMUM_HRS_IN_QUEUE) {
        long beforeFlush = System.currentTimeMillis();
        this.buffer = new ConcurrentLinkedQueue<HeartRateSample>(this.queue);
        LOG.info("queue is full ({})- buffering data, storing {} hrs in the database", this.queue.size(), this.buffer.size());
        this.queue.clear();
        while (!this.buffer.isEmpty()) {
          storeSample(this.buffer.poll());
        }
        this.buffer = null;
        long afterFlush = System.currentTimeMillis();
        LOG.info("done in {} - trying to upload hrs again", (afterFlush - beforeFlush));
      }
      if (!this.queue.isEmpty()) {
        LOG.info("queue size is {}", this.queue.size());
        HeartRateSample hrs = this.queue.poll();
        if (this.hwPlatform.isUploadPermitted()) {
          uploadSample(hrs);
        }
        storeSample(hrs);
        continue;
      }
      try {
        Thread.sleep(UPLOAD_THREAD_SLEEP_TIME_MS);
      } catch (InterruptedException e) {
        LOG.info("interrupt received from Controller");
      }
    }
    if (this.buffer != null) {
      if (!this.buffer.isEmpty()) {
        LOG.info("storing remaining hrs from the buffer");
        LOG.info("buffer size: {}", this.queue.size());
      }
      while (!this.buffer.isEmpty()) {
        storeSample(this.buffer.poll());
      }
    }
    if (!this.queue.isEmpty()) {
      LOG.info("storing remaining hrs from the queue");
      LOG.info("queue size: {}", this.queue.size());
    }
    while (!this.queue.isEmpty()) {
      storeSample(this.queue.poll());
    }
    LOG.info("thread finished");
  }

  /**
   * Add a heart rate sample to the queue for storing it in the database and uploading.
   *
   * @param hrs
   *          HeartRateSample retrieved from the sensor
   */
  public void addToQueue(HeartRateSample hrs) {
    this.queue.add(hrs);
  }

  /**
   * Expose the total number of all heart rate samples in the queue for the manual upload in Controller.
   *
   * @return number of heart rate samples
   */
  public int getNumberOfHrsInQueue() {
    return this.queue.size();
  }

  /**
   * Try to upload a HeartRateSample to an external webinterface.
   *
   * @param hrs
   *          HeartRateSample from the queue
   */
  private void uploadSample(HeartRateSample hrs) {
    if (this.uploader != null) {
      try {
        int statusCode = this.uploader.sendHeartRateSample(hrs);
        switch (statusCode) {
          case HttpStatus.SC_CREATED:
            hrs.setTransmitted(true);
            break;
          case HttpStatus.SC_OK:
            hrs.setTransmitted(true);
            break;
          case HttpStatus.SC_UNAUTHORIZED:
            LOG.error("transmission unauthorized - attempting to log in again");
            this.uploader.login();
            break;
          default:
            LOG.error("transmission failed, error code: {}", statusCode);
            break;
        }
      } catch (IOException e) {
        LOG.error("http client execute (sample transmission) failed", e.getMessage());
      }
    } else {
      LOG.warn("no uploader specified");
    }
  }

  /**
   * Save a heart rate sample to the database.
   *
   * @param sample
   *          a HeartRateSample from the queue
   */
  private void storeSample(HeartRateSample hrs) {
    DBsession dbs = this.dbc.getSession();
    dbs.saveHeartRateSample(hrs);
    dbs.commit();
    dbs.close();
  }

  /**
   * Method for uploading all heart rate samples in the queue to the specified webinterface. This method is intended to be blocking and only called from the
   * webinterface.
   */
  public void manualUpload() {
    if (this.uploader != null) {
      this.uploader.login();
    }
    while (!this.queue.isEmpty()) {
      HeartRateSample hrs = this.queue.poll();
      uploadSample(hrs);
      storeSample(hrs);
    }
  }

}
