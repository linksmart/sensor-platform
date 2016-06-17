package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Helper class for sensor connection setup.
 *
 * @author Daniel Pyka
 *
 */
public class SensorDescription {

  private SensorType type;
  private String name;
  private String bdAddress;

  public SensorDescription(SensorType type, String name, String bdAddress) {
    this.type = type;
    this.name = name;
    this.bdAddress = bdAddress;
  }

  public SensorDescription(String name, String bdAddress) {
    this.type = SensorType.UNDEFINED;
    this.name = name;
    this.bdAddress = bdAddress;
  }

  public SensorDescription(SensorType type, String bdAddress) {
    this.type = type;
    this.name = "unknown";
    this.bdAddress = bdAddress;
  }

  public SensorType getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public String getBdAddress() {
    return this.bdAddress;
  }

  public void setType(SensorType type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBdAddress(String bdAddress) {
    this.bdAddress = bdAddress;
  }

  @Override
  public String toString() {
    return "{\"type\":\"" + this.type + "\",\"name\":\"" + this.name + "\",\"bdaddress\":\"" + this.bdAddress + "\"}";
  }
}
