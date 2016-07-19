package de.fhg.fit.biomos.sensorplatform.sample;

public abstract class Sample {

  protected final String timestamp;

  public Sample(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getTimestamp() {
    return this.timestamp;
  }
}
