package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;
import java.util.Properties;

import org.json.JSONObject;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.sensors.SensorCommands}
 *
 * @author Daniel Pyka
 *
 */
public abstract class Sensor implements SensorCommands {

  protected final Properties properties;
  protected final SensorName name;
  protected final String bdAddress;
  protected final AddressType addressType;

  protected BufferedWriter bw = null;

  public Sensor(Properties properties, SensorName name, String bdAddress, AddressType addressType) {
    this.properties = properties;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
  }

  public SensorName getName() {
    return this.name;
  }

  public String getBdaddress() {
    return this.bdAddress;
  }

  public AddressType getAddressType() {
    return this.addressType;
  }

  /**
   * Link the specific sensor to the input stream of a gatttool to send commands to the physical sensor.
   *
   * @param bw
   */
  public void hook(BufferedWriter bw) {
    this.bw = bw;
  }

  public void unhook() {
    this.bw = null;
  }

  @Override
  public String toString() {
    return new JSONObject().put("name", this.name).put("bdaddress", this.bdAddress).put("addresstype", this.addressType).toString();
  }

}
