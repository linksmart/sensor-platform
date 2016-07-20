package de.fhg.fit.biomos.sensorplatform.sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all available data from a single heart rate sample.
 *
 * @author Daniel Pyka
 *
 */
public class HeartRateSample extends Sample {

  private final String UNIT_BEATS_PER_MINUTE = "bpm";
  private final String UNIT_JOULE = "J";
  private final String UNIT_BPM_MS = "bpm/ms";

  private int heartRate = 0;
  private int energyExpended = 0;
  private List<Integer> rrIntervals = new ArrayList<Integer>();

  public HeartRateSample(String timestamp, String bdAddress) {
    super(timestamp, bdAddress);
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
    return "{\"timestamp\":\"" + this.timestamp + "\",\"device\":\"" + this.bdAddress + "\",\"value\":" + "{\"heartrate\":{\"value\":" + this.heartRate
        + ",\"unit\":\"" + this.UNIT_BEATS_PER_MINUTE + "\"},\"energyexpended\":{\"value\":" + this.energyExpended + ",\"unit\":\"" + this.UNIT_JOULE + "\"}"
        + ",\"rrintervals\":{\"value\":" + this.rrIntervals.toString() + ",\"unit\":\"" + this.UNIT_BPM_MS + "\"}}}";

  }

}
