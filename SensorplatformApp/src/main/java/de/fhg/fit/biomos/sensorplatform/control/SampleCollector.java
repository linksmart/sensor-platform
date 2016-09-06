package de.fhg.fit.biomos.sensorplatform.control;

/**
 *
 * @author Daniel Pyka
 *
 */
public interface SampleCollector extends Runnable {

  public boolean getActiveFlag();

  public void setActiveFlag(boolean active);

}
