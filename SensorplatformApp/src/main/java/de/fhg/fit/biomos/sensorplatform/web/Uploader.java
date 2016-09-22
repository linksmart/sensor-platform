package de.fhg.fit.biomos.sensorplatform.web;

import java.io.IOException;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 * If there are plans to support additional webinterface in the sensorplatform in the future, this interface may become useful. Username, password, cookies and
 * all other data should be stored in the class which implements this interface. Since there is only one webinterface with a fixed, predefined workflow, this
 * interface may need to be changed to properly fit for connection to different webinterfaces. Implementations of this interface will be bound by Guice to the
 * Uploader interface, dependent on the project setup. An Uploader is intended to be used as Singleton, enforced by Guice.
 *
 * @author Daniel Pyka
 *
 */
public interface Uploader {

  /**
   * Retrieve the name (as defined in the parent pom) of the webinterface. Only used for logging.
   *
   * @return String the webinterface name
   */
  public String getWebinterfaceName();

  /**
   * Login to the webinterface (token based for example) if required.
   */
  public void login();

  /**
   * Send a POST http request to the webinterface to upload a sample.
   *
   * @param hrs
   *          HeartRateSample extract the fields which are required in the data structure of the webinterface
   * @return http response code http response code to see what happens to the request
   * @throws IOException
   *           reserved for http client exception
   */
  public int sendHeartRateSample(HeartRateSample hrs) throws IOException;

}
