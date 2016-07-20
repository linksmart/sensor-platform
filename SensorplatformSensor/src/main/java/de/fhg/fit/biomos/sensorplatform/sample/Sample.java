package de.fhg.fit.biomos.sensorplatform.sample;

public abstract class Sample {

  protected final String timestamp;
  protected final String bdAddress;

  public Sample(String timestamp, String bdAddress) {
    this.timestamp = timestamp;
    this.bdAddress = bdAddress;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public String getBDaddress() {
    return this.bdAddress;
  }
}
