package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
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

  private final SensorConfiguration sensorConfiguration;

  Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  public CC2650(Properties properties, SensorName name, String bdAddress, AddressType addressType, SensorType sensorType,
      SensorConfiguration sensorConfiguration) {
    super(properties, name, bdAddress, addressType, sensorType);

    this.sensorConfiguration = sensorConfiguration;
  }

  private void enableTemperatureNotification() {
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
      LOG.info("enable temperature notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableTemperatureNotification() {
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
      LOG.info("disable temperature notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enableHumidityNotification() {
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
      LOG.info("enable humidity notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableHumidityNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + CC2650lib.INTERVAL_HUMIDITY_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable humidity notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enableLightNotification() {
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
      LOG.info("enable ambientlight notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableLightNotification() {
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
      LOG.info("disable ambientlight notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void enablePressureNotification() {
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
      LOG.info("enable pressure notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disablePressureNotification() {
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
      LOG.info("disable pressure notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * TODO Fixed acceleration range at -8, +8 G
   */
  private void enableMovementNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + this.sensorConfiguration.getSetting("movement"));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + CC2650lib.VALUE_MOVEMENT_ACTIVATE_ALL_16G);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable movement notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableMovementNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + CC2650lib.VALUE_MOVEMENT_DEACTIVATE_ALL_16G);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + CC2650lib.INTERVAL_MOVEMENT_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable movement notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableLogging() {
    if (this.sensorConfiguration.containsSetting("irtemperature")) {
      setupFileLogger("irtemperature");
      enableTemperatureNotification();
    }
    if (this.sensorConfiguration.containsSetting("humidity")) {
      setupFileLogger("humidity");
      enableHumidityNotification();
    }
    if (this.sensorConfiguration.containsSetting("ambientlight")) {
      setupFileLogger("ambientlight");
      enableLightNotification();
    }
    if (this.sensorConfiguration.containsSetting("pressure")) {
      setupFileLogger("pressure");
      enablePressureNotification();
    }
    if (this.sensorConfiguration.containsSetting("movement")) {
      setupFileLogger("movement");
      enableMovementNotification();
    }
  }

  @Override
  public void disableLogging() {
    if (this.sensorConfiguration.containsSetting("irtemperature")) {
      disableTemperatureNotification();
      this.sampleLoggers.get("irtemperature").close();
    }
    if (this.sensorConfiguration.containsSetting("humidity")) {
      disableHumidityNotification();
      this.sampleLoggers.get("humidity").close();
    }
    if (this.sensorConfiguration.containsSetting("ambientlight")) {
      disableLightNotification();
      this.sampleLoggers.get("ambientlight").close();
    }
    if (this.sensorConfiguration.containsSetting("pressure")) {
      disablePressureNotification();
      this.sampleLoggers.get("pressure").close();
    }
    if (this.sensorConfiguration.containsSetting("movement")) {
      disableMovementNotification();
      this.sampleLoggers.get("movement").close();
    }
  }

  private void setupFileLogger(String measure) {
    this.sampleLoggers.put(measure, new SampleLogger(this.properties, measure, this.name.name()));
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
    float temp = rawTemp / 100.0f;
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
    float press = rawPress / 100.0f;
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

    return "Ambientlight: " + light + "lx";
  }

  /**
   * TODO Values need a lot of post-processing. Sensor values have spikes to 500 deg/s and some noise.
   *
   * @param data
   * @return Rotation in deg/s (degrees per second), range -250, +250
   */
  private String getRotation(String data) {
    float rotationX = Math.round((Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    float rotationY = Math.round((Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    float rotationZ = Math.round((Integer.parseInt(data.substring(10, 12) + data.substring(8, 10), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    return "Rotation X:" + rotationX + "deg/s Y:" + rotationY + "deg/s Z:" + rotationZ + "deg/s";
  }

  /**
   * TODO Acceleration range fixed at -8, +8 G for now, when activating movement logging
   *
   * @param data
   * @return Acceleration in G
   */
  private String getAcceleration(String data) {
    float accX = Math.round((Integer.parseInt(data.substring(14, 16) + data.substring(12, 14), 16) * 1.0f) / (32768 / 16) * 100) / 100.0f;
    float accY = Math.round((Integer.parseInt(data.substring(18, 20) + data.substring(16, 18), 16) * 1.0f) / (32768 / 16) * 100) / 100.0f;
    float accZ = Math.round((Integer.parseInt(data.substring(22, 24) + data.substring(20, 22), 16) * 1.0f) / (32768 / 16) * 100) / 100.0f;
    return "Acceleration X:" + accX + "G Y:" + accY + "G Z:" + accZ + "G";
  }

  /**
   * May need calibration before using the values.
   *
   * @param data
   * @return Magnetism in uT (micro Tesla), range +-4900
   */
  private String getMagnetism(String data) {
    int magX = Integer.parseInt(data.substring(26, 28) + data.substring(24, 26), 16);
    int magY = Integer.parseInt(data.substring(30, 32) + data.substring(28, 30), 16);
    int magZ = Integer.parseInt(data.substring(34, 36) + data.substring(32, 34), 16);
    return "Magnetism X:" + magX + "uT Y:" + magY + "uT Z:" + magZ + "uT";
  }

  // TODO split different measurement types from one sensor to different files
  @Override
  public void processSensorData(String handle, String data) {
    data = data.replace(" ", "");
    String value;
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        value = getIRtemperatureFromTemperatureSensor(data);
        this.sampleLoggers.get("irtemperature").write(value);
        value = getDieTemperatureFromTemperatureSensor(data);
        this.sampleLoggers.get("irtemperature").write(value);
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        value = getTemperatureFromBarometricPressureSensor(data);
        this.sampleLoggers.get("pressure").write(value);
        value = getPressure(data);
        this.sampleLoggers.get("pressure").write(value);
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        value = getAmbientLight(data);
        this.sampleLoggers.get("ambientlight").write(value);
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        value = getTemperatureFromHumiditySensor(data);
        this.sampleLoggers.get("humidity").write(value);
        value = getRelativeHumidty(data);
        this.sampleLoggers.get("humidity").write(value);
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        value = getRotation(data);
        this.sampleLoggers.get("movement").write(value);
        value = getAcceleration(data);
        this.sampleLoggers.get("movement").write(value);
        value = getMagnetism(data);
        this.sampleLoggers.get("movement").write(value);
        break;
      default:
        LOG.error("unexpected handle notification " + handle + " : " + data);
        break;
    }
  }

}
