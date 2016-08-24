package de.fhg.fit.biomos.sensorplatform.deprecated;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
public class BluetoothDevice {
  private final String deviceName;
  private final String bdAddress;
  private final AddressType addressType;
  private final SecurityLevel secLevel;

  public BluetoothDevice(String deviceName, String bdAddress, AddressType addressType, SecurityLevel secLevel) {
    this.deviceName = deviceName;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.secLevel = secLevel;
  }

  public String getDeviceName() {
    return this.deviceName;
  }

  public String getBDAddress() {
    return this.bdAddress;
  }

  public AddressType getAddressType() {
    return this.addressType;
  }

  public SecurityLevel getSecLevel() {
    return this.secLevel;
  }

  @Override
  public String toString() {
    return this.deviceName + ", " + this.bdAddress + ", " + this.addressType + ", " + this.secLevel;
  }

}
