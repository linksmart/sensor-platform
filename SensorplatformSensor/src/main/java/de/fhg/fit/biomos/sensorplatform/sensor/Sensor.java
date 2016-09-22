package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;

import de.fhg.fit.biomos.sensorplatform.gatt.GattLibrary;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for all sensors which may communicate with the sensorplatform. Contains general information like the name, bluetooth address and so on.
 *
 * @author Daniel Pyka
 *
 */
public abstract class Sensor<T extends GattLibrary> implements SensorCommands {

  protected final T gattLibrary;
  protected final SensorName name;
  protected final String bdAddress;
  protected final AddressType addressType;
  protected final SecurityLevel securityLevel;
  protected final JSONObject settings;

  public Sensor(T gattLibrary, SensorName name, String bdAddress, AddressType addressType, SecurityLevel securityLevel, JSONObject settings) {
    this.gattLibrary = gattLibrary;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.securityLevel = securityLevel;
    this.settings = settings;
  }

  public SensorName getName() {
    return this.name;
  }

  public String getBDaddress() {
    return this.bdAddress;
  }

  public AddressType getAddressType() {
    return this.addressType;
  }

  public SecurityLevel getSecurityLevel() {
    return this.securityLevel;
  }

  public JSONObject getSettings() {
    return this.settings;
  }

  @Override
  public String toString() {
    return this.name + " " + this.bdAddress;
  }

}
