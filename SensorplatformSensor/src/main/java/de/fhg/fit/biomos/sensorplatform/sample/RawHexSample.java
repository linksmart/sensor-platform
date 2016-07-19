package de.fhg.fit.biomos.sensorplatform.sample;

public class RawHexSample {

  private final String address;
  private final String hexData;

  public RawHexSample(String address, String hexData) {
    this.address = address;
    this.hexData = hexData;
  }

  public String getAddress() {
    return this.address;
  }

  public String getHexData() {
    return this.hexData;
  }

  @Override
  public String toString() {
    return this.address + " " + this.hexData;
  }

}
