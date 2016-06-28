package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;

import org.json.JSONObject;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.SensorType;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.sensors.SensorCommands}
 *
 * @author Daniel Pyka
 *
 */
public abstract class Sensor implements SensorCommands {

  protected final SensorName name;
  protected final String bdAddress;
  protected final AddressType addressType;
  protected final SensorType sensorType;

  protected BufferedWriter bw = null;

  public Sensor(SensorName name, String bdAddress, AddressType addressType, SensorType sensorType) {
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.sensorType = sensorType;
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

  public SensorType getSensorType() {
    return this.sensorType;
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
    return new JSONObject().put("name", this.name).put("bdaddress", this.bdAddress).put("addresstype", this.addressType).put("sensortype", this.sensorType)
        .toString();
  }

}
