package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractSensor<T extends GattLibrary> implements SensorCommands {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractSensor.class);

  protected final T gattLibrary;
  protected final SensorName name;
  protected final String bdAddress;
  protected final AddressType addressType;
  protected final SecurityLevel securityLevel;
  protected final JSONObject settings;

  public AbstractSensor(T gattLibrary, SensorName name, String bdAddress, AddressType addressType, SecurityLevel securityLevel, JSONObject settings) {
    this.gattLibrary = gattLibrary;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.securityLevel = securityLevel;
    this.settings = settings;
  }

  public T getGattLibrary() {
    return this.gattLibrary;
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
  public String requestBatteryLevel(BufferedWriter streamToSensor, String charReadCmd) {
    String handle = this.gattLibrary.getHandleBatteryLevel();
    if (handle != null) {
      try {
        streamToSensor.write(charReadCmd + " " + handle);
        streamToSensor.newLine();
        streamToSensor.flush();
        LOG.info("read battery level");
      } catch (IOException e) {
        LOG.error("cannot read battery level", e);
      }
    } else {
      LOG.error("battery level is not supported by the gatt server");
    }
    return handle;
  }

  @Override
  public int calculateBatteryLevel(String rawHexValues) {
    return Integer.parseInt(rawHexValues, 16);
  }

  @Override
  public String toString() {
    return this.name + " " + this.bdAddress;
  }

}
