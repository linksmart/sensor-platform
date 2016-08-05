package de.fhg.fit.biomos.sensorplatform.control;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

public interface SampleCollector extends Runnable {

  public void addToQueue(HeartRateSample hrs);
}
