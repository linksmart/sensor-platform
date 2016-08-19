package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public class BluetoothDevice {
  private final String deviceName;
  private final String bdAddress;
  private final AddressType addressType;
  private final GatttoolSecurityLevel secLevel;

  public BluetoothDevice(String deviceName, String bdAddress, AddressType addressType, GatttoolSecurityLevel secLevel) {
    this.deviceName = deviceName;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.secLevel = secLevel;
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

  public GatttoolSecurityLevel getSecLevel() {
    return this.secLevel;
  }

  @Override
  public String toString() {
    return this.deviceName + ", " + this.bdAddress + ", " + this.addressType + ", " + this.secLevel;
  }

}
