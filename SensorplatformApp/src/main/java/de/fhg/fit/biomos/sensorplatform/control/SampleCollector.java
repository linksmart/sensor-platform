package de.fhg.fit.biomos.sensorplatform.control;

public interface SampleCollector extends Runnable {

  public boolean getStartFlag();

  public void setStartFlag(boolean start);

}
