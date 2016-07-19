package de.fhg.fit.biomos.sensorplatform.sample;

import java.util.List;

/**
 * Stores all available data from a single heart rate sample.
 *
 * @author Daniel Pyka
 *
 */
public class HeartRateSample extends Sample {

  private int heartRate;
  private int energyExpended;
  private List<Integer> rrIntervals;

  public HeartRateSample(String timestamp) {
    super(timestamp);
  }

  public int getHeartRate() {
    return this.heartRate;
  }

  public void setHeartRate(int heartRate) {
    this.heartRate = heartRate;
  }

  public int getEnergyExpended() {
    return this.energyExpended;
  }

  public void setEnergyExpended(int energyExpended) {
    this.energyExpended = energyExpended;
  }

  public List<Integer> getRRintervals() {
    return this.rrIntervals;
  }

  public void setRRinterval(List<Integer> rrIntervals) {
    this.rrIntervals = rrIntervals;
  }

  @Override
  public String toString() {
    return "{\"timestamp\":\" " + this.timestamp + "\",\"heartrate\":\"" + this.heartRate + "\",\"rrintervals\":" + this.rrIntervals.toString() + "}";
  }

}
