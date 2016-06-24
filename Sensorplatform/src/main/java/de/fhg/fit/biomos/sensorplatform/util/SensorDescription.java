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
  private AddressType addressType;

  public SensorDescription(SensorType type, String name, String bdAddress, AddressType addressType) {
    this.type = type;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
  }

  public SensorDescription(String name, String bdAddress, AddressType addressType) {
    this.type = SensorType.UNDEFINED;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
  }

  public SensorDescription(SensorType type, String bdAddress, AddressType addressType) {
    this.type = type;
    this.name = "unknown";
    this.bdAddress = bdAddress;
    this.addressType = addressType;
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

  public AddressType getAddressType() {
    return this.addressType;
  }

  public void setAddressType(AddressType addressType) {
    this.addressType = addressType;
  }

  @Override
  public String toString() {
    return "{\"type\":\"" + this.type + "\",\"name\":\"" + this.name + "\",\"bdaddress\":\"" + this.bdAddress + "\",\"addresstype\":\"" + this.addressType
        + "\"}";
  }
}
