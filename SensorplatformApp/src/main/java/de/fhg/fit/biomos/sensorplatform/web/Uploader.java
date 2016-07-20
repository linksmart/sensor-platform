package de.fhg.fit.biomos.sensorplatform.web;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 * If there are plans to support additional webinterface in the sensorplatform in the future, this interface may become useful. Username, password, cookies and
 * all other data should be stored in the class which implements this interface. Since there is only one webinterface with a fixed, predefined workflow, this
 * interface may need to be changed to properly fit for connection to different webinterfaces.
 *
 * @author Daniel Pyka
 *
 */
public interface Uploader extends Runnable {

  /**
   * Add the next sample to the upload queue.
   */
  public void addToQueue(HeartRateSample hrs);

}
