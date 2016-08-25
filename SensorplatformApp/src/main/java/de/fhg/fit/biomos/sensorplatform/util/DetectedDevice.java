package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public class DetectedDevice {

  private final String name;
  private final String bdAddress;

  public DetectedDevice(String name, String bdAddress) {
    this.name = name;
    this.bdAddress = bdAddress;
  }

  public String getName() {
    return this.name;
  }

  public String getBdAddress() {
    return this.bdAddress;
  }

  @Override
  public String toString() {
    return "{\"name\":\"" + this.name + "\",\"bdaddress\":\"" + this.bdAddress + "\"}";
  }

}
