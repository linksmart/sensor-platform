package de.fhg.fit.biomos.sensorplatform.control;

/**
 * A SampleCollector decides, what to do further with a new calculated Sample. Usually this means storing it in the database. A single SampleCollector may be
 * shared among multiple SensorWrapper instances. The Runnable interface is used to execute storing and uploading in a separate thread.
 *
 * @author Daniel Pyka
 *
 */
public interface SampleCollector extends Runnable {

  /**
   * The Controller needs to know if the recording period requires a specific SampleCollector (for example using the C2650 sensor requires the
   * CC2650SampleCollector) to be started.
   *
   * @return true if a sensorcollector is used for the recording period, false otherwise
   */
  public boolean isUsed();

  /**
   * The flag is set during creation of the sensorwrapper list in the SensorWrapperFactory and is checked in the Controller at startup of a new recording
   * period. This is also the interupt flag for the SampleCollector to finish a thread gracefully.
   *
   * @param used
   *          true if the SampleCollector is used, false otherwise
   */
  public void setUsed(boolean used);

}
