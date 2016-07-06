package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorConfiguration;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.SensorType;

/**
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @author Daniel Pyka
 *
 */
public class CC2650 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650.class);

  private final SimpleDateFormat formatter;

  private final File irTempFile;
  private final File humidityFile;
  private final File ambientlightFile;
  private final File pressureFile;

  private PrintWriter irTempWriter = null;
  private PrintWriter humWriter = null;
  private PrintWriter ambientlightWriter = null;
  private PrintWriter pressureWriter = null;

  private final SensorConfiguration sensorConfiguration;

  public CC2650(Properties properties, SensorName name, String bdAddress, AddressType addressType, SensorType sensorType,
      SensorConfiguration sensorConfiguration) {
    super(properties, name, bdAddress, addressType, sensorType);

    this.formatter = new SimpleDateFormat(properties.getProperty("ditg.webinterface.timestamp.format"));

    this.sensorConfiguration = sensorConfiguration;

    this.irTempFile = new File(new File(new File(properties.getProperty("sensors.data.directory"), this.name.name()), "irtemperature"), "irtemperature.log");
    this.humidityFile = new File(new File(new File(properties.getProperty("sensors.data.directory"), this.name.name()), "humidity"), "humidity.log");
    this.ambientlightFile = new File(new File(new File(properties.getProperty("sensors.data.directory"), this.name.name()), "ambientlight"),
        "ambientlight.log");
    this.pressureFile = new File(new File(new File(properties.getProperty("sensors.data.directory"), this.name.name()), "pressure"), "pressure.log");

    if (this.irTempFile.exists()) {
      this.irTempFile.delete();
    } else {
      this.irTempFile.getParentFile().mkdirs();
    }
    if (this.humidityFile.exists()) {
      this.humidityFile.delete();
    } else {
      this.humidityFile.getParentFile().mkdirs();
    }
    if (this.ambientlightFile.exists()) {
      this.ambientlightFile.delete();
    } else {
      this.ambientlightFile.getParentFile().mkdirs();
    }
    if (this.pressureFile.exists()) {
      this.pressureFile.delete();
    } else {
      this.pressureFile.getParentFile().mkdirs();
    }

    try {
      this.irTempWriter = new PrintWriter(this.irTempFile, "UTF-8");
      this.irTempWriter.println("# " + name);
      LOG.info("using log file: " + this.irTempFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      this.humWriter = new PrintWriter(this.humidityFile, "UTF-8");
      this.humWriter.println("# " + name);
      LOG.info("using log file: " + this.humidityFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      this.ambientlightWriter = new PrintWriter(this.ambientlightFile, "UTF-8");
      this.ambientlightWriter.println("# " + name);
      LOG.info("using log file: " + this.ambientlightFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      this.pressureWriter = new PrintWriter(this.pressureFile, "UTF-8");
      this.pressureWriter.println("# " + name);
      LOG.info("using log file: " + this.pressureFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enableTemperatureLogging() {
    try {
      this.bw
          .write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + this.sensorConfiguration.getSetting("irtemperature"));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable temperature logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableTemperatureLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable temperature logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enableHumidityLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + this.sensorConfiguration.getSetting("humidity"));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable humidity logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableHumidityLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable humidity logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enableLightLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + this.sensorConfiguration.getSetting("light"));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable ambientlight logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableLightLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + CC2650lib.INTERVAL_AMBIENTLIGHT_800MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable ambientlight logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enablePressureLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + this.sensorConfiguration.getSetting("pressure"));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable pressure logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disablePressureLogging() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + CC2650lib.INTERVAL_PRESSURE_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable pressure logging");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableLogging() {
    if (this.sensorConfiguration.containsSetting("irtemperature")) {
      enableTemperatureLogging();
    }
    if (this.sensorConfiguration.containsSetting("humidity")) {
      enableHumidityLogging();
    }
    if (this.sensorConfiguration.containsSetting("ambientlight")) {
      enableLightLogging();
    }
    if (this.sensorConfiguration.containsSetting("pressure")) {
      enablePressureLogging();
    }
    if (this.sensorConfiguration.containsSetting("acceleration")) {
      // TODO
    }
  }

  @Override
  public void disableLogging() {
    disableTemperatureLogging();
    disableHumidityLogging();
    disableLightLogging();
    disablePressureLogging();
    this.irTempWriter.flush();
    this.irTempWriter.close();
    this.humWriter.flush();
    this.humWriter.close();
    this.ambientlightWriter.flush();
    this.ambientlightWriter.close();
    this.pressureWriter.flush();
    this.pressureWriter.close();
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getIRtemperatureFromTemperatureSensor(String data) {
    String valHex = data.substring(0, 4);
    valHex = (valHex + valHex.substring(0, 2)).substring(2, 6);

    int val = (Integer.parseInt(valHex, 16)) >>> 2;
    float object = Math.round(val * 0.03125f * 10) / 10.0f;

    return "Object temperature (IR): " + object + "°C";
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getDieTemperatureFromTemperatureSensor(String data) {
    String valHex = data.substring(4, 8);
    valHex = (valHex + valHex.substring(0, 2)).substring(2, 6);

    int val = (Integer.parseInt(valHex, 16)) >>> 2;
    float ambience = Math.round(val * 0.03125f * 10) / 10.0f;

    return "Die temperature: " + ambience + "°C";
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getTemperatureFromBarometricPressureSensor(String data) {
    int rawTemp = Integer.parseInt(data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2), 16);
    Float temp = rawTemp / 100.0f;
    return "Temperature (press): " + temp + "°C";
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Pressure in hectopascal (hPa)
   */
  private String getPressure(String data) {
    int rawPress = Integer.parseInt(data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8), 16);
    Float press = rawPress / 100.0f;
    return "Pressure: " + press + "hPa";
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getTemperatureFromHumiditySensor(String data) {
    String val = data.substring(0, 4);
    val = (val + val.substring(0, 2)).substring(2, 6);

    float temp = Math.round(((((float) Integer.parseInt(val, 16)) / 65536) * 165 - 40) * 10) / 10.0f;

    return "Temperature (hum): " + temp + "°C";
  }

  /**
   *
   * @param data
   * @return Relative Humidity (%RH)
   */
  private String getRelativeHumidty(String data) {
    String val = data.substring(4, 8);
    val = (val + val.substring(0, 2)).substring(2, 6);

    float hum = Math.round(((((float) Integer.parseInt(val, 16)) / 65536) * 100) * 10) / 10.0f;

    return "Relative humidity: " + hum + "%RH";
  }

  /**
   *
   * @param data
   * @return Light intensity in LUX
   */
  private String getAmbientLight(String data) {
    String val = data.substring(0, 4);
    val = (val + val.substring(0, 2)).substring(2, 6);

    int raw = Integer.parseInt(val, 16);

    int m = raw & 0x0FFF;
    int e = (raw & 0xF000) >>> 12;

    float light = (float) (m * (0.01 * Math.pow(2.0, e)));

    return "Ambientlight: " + light + " lx";
  }

  // TODO split different measurement types from one sensor to different files
  @Override
  public void processSensorData(String handle, String data) {
    data = data.replace(" ", "");
    String value;
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getIRtemperatureFromTemperatureSensor(data);
        this.irTempWriter.println(value);
        System.out.println(value);
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getDieTemperatureFromTemperatureSensor(data);
        this.irTempWriter.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getTemperatureFromBarometricPressureSensor(data);
        this.pressureWriter.println(value);
        System.out.println(value);
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getPressure(data);
        this.pressureWriter.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getAmbientLight(data);
        this.ambientlightWriter.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getTemperatureFromHumiditySensor(data);
        this.humWriter.println(value);
        System.out.println(value);
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + getRelativeHumidty(data);
        this.humWriter.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        value = this.formatter.format(Calendar.getInstance().getTime()) + " " + data;
        System.out.println(value);
        break;
      default:
        LOG.error("unexpected handle notification " + handle);
        break;
    }
  }

}
