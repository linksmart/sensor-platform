package de.fhg.fit.biomos.sensorplatform.control;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface SampleCollector extends Runnable {

  public boolean isUsed();

  public void setUsed(boolean active);

}
