package de.fhg.fit.biomos.sensorplatform.util;

public class BluetoothDevice {
  private final String deviceName;
  private final String bdAddress;
  private final AddressType addressType;
  private boolean paired;
  private boolean authenticated;

  public BluetoothDevice(String deviceName, String bdAddress, AddressType addressType) {
    this.deviceName = deviceName;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.paired = false;
    this.authenticated = false;
  }

  public String getDeviceName() {
    return this.deviceName;
  }

  public String getBdAddress() {
    return this.bdAddress;
  }

  public AddressType getAddressType() {
    return this.addressType;
  }

  public boolean isPaired() {
    return this.paired;
  }

  public void setPaired(boolean paired) {
    this.paired = paired;
  }

  public boolean isAuthenticated() {
    return this.paired;
  }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  @Override
  public String toString() {
    return this.deviceName + ", " + this.bdAddress + ", " + this.addressType + ", " + this.paired + ", " + this.authenticated;
  }

}
