package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;
import java.util.Properties;

import org.json.JSONObject;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for all sensors which may communicate with the sensorplatform.
 *
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
   * Link the specific sensor to the input stream of a gatttool process to send commands to the physical sensor.
   *
   * @param bw
   */
  public void hook(BufferedWriter bw) {
    this.bw = bw;
  }

  /**
   * Disconnect the sensor object from the input stream of the gatttool process. This is not required, but keeps the workflow clean!
   */
  public void unhook() {
    this.bw = null;
  }

  @Override
  public String toString() {
    return new JSONObject().put("name", this.name).put("bdaddress", this.bdAddress).put("addresstype", this.addressType).toString();
  }

}
