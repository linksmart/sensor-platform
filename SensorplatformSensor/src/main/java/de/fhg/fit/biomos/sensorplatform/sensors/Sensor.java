package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

  protected final SensorName name;
  protected final String bdAddress;
  protected final AddressType addressType;

  protected final JSONObject settings;

  protected BufferedWriter bw = null;

  protected final DateTimeFormatter dtf;

  public Sensor(SensorName name, String bdAddress, AddressType addressType, String timestampFormat, JSONObject settings) {
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;

    this.settings = settings;

    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);
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

  public JSONObject getSettings() {
    return this.settings;
  }

  @Override
  public String toString() {
    return new JSONObject().put("name", this.name).put("bdaddress", this.bdAddress).put("addresstype", this.addressType).toString();
  }

}
