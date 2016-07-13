package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.json.JSONObject;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorConfiguration;
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
  protected final boolean consoleLogging;
  protected final boolean fileLogging;
  protected final String webinterface;
  protected final JSONObject measuresConfiguration;

  protected BufferedWriter bw = null;

  protected final SimpleDateFormat formatter;

  public Sensor(Properties properties, SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
    this.properties = properties;
    this.name = name;
    this.bdAddress = bdAddress;
    this.addressType = addressType;

    JSONObject loggingConfiguration = sensorConfiguration.getJSONObject(SensorConfiguration.LOGGING);
    this.consoleLogging = loggingConfiguration.getBoolean(SensorConfiguration.CONSOLE);
    this.fileLogging = loggingConfiguration.getBoolean(SensorConfiguration.FILE);
    this.webinterface = loggingConfiguration.getString(SensorConfiguration.WEBINTERFACE);

    this.measuresConfiguration = sensorConfiguration.getJSONObject(SensorConfiguration.MEASURES);

    this.formatter = new SimpleDateFormat(properties.getProperty("ditg.webinterface.timestamp.format"));
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
