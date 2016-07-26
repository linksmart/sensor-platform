package de.fhg.fit.biomos.sensorplatform.sample;

public abstract class Sample {

  protected final String timestamp;
  protected final String bdAddress;
  protected boolean transmitted;

  public Sample(String timestamp, String bdAddress, boolean transmitted) {
    this.timestamp = timestamp;
    this.bdAddress = bdAddress;
    this.transmitted = transmitted;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public String getBDaddress() {
    return this.bdAddress;
  }

  public boolean isTransmitted() {
    return this.transmitted;
  }

  public void setTransmitte(boolean transmitted) {
    this.transmitted = transmitted;
  }
}
