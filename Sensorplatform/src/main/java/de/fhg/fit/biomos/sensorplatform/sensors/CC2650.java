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

/**
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib}
 *
 * @author Daniel Pyka
 *
 */
public class CC2650 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650.class);

  private final SensorConfiguration sensorConfiguration;

  Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  public CC2650(Properties properties, SensorName name, String bdAddress, AddressType addressType, SensorConfiguration sensorConfiguration) {
    super(properties, name, bdAddress, addressType);

    this.sensorConfiguration = sensorConfiguration;
  }

  /**
   * Enable temperature sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableTemperatureNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " "
          + this.sensorConfiguration.getSetting(SensorConfiguration.IRTEMPERATURE));
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

  /**
   * Disable temperature sensor measurement, notification and reset the notification peroid to the default value.
   */
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

  /**
   * Enable humidity sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableHumidityNotification() {
    try {
      this.bw.write(
          GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + this.sensorConfiguration.getSetting(SensorConfiguration.HUMIDITY));
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

  /**
   * Disable humidity sensor measurement, notification and reset the notification peroid to the default value.
   */
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

  /**
   * Enable ambientlight sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableAmbientlightNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " "
          + this.sensorConfiguration.getSetting(SensorConfiguration.AMBIENTLIGHT));
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

  /**
   * Disable ambientlight sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disableAmbientlightNotification() {
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

  /**
   * Enable pressure sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enablePressureNotification() {
    try {
      this.bw.write(
          GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + this.sensorConfiguration.getSetting(SensorConfiguration.PRESSURE));
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

  /**
   * Disable pressure sensor measurement, notification and reset the notification peroid to the default value.
   */
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
   * Enable movement sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   *
   * TODO Fixed acceleration range at -8, +8 G
   */
  private void enableMovementNotification() {
    try {
      this.bw.write(
          GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + this.sensorConfiguration.getSetting(SensorConfiguration.MOVEMENT));
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

  /**
   * Disable movement sensor measurement, notification and reset the notification peroid to the default value.
   */
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
  public void enableNotification() {
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.IRTEMPERATURE)) {
      this.sampleLoggers.put(SensorConfiguration.IRTEMPERATURE, new SampleLogger(this.properties, SensorConfiguration.IRTEMPERATURE, this.name.name()));
      enableTemperatureNotification();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.HUMIDITY)) {
      this.sampleLoggers.put(SensorConfiguration.HUMIDITY, new SampleLogger(this.properties, SensorConfiguration.HUMIDITY, this.name.name()));
      enableHumidityNotification();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.AMBIENTLIGHT)) {
      this.sampleLoggers.put(SensorConfiguration.AMBIENTLIGHT, new SampleLogger(this.properties, SensorConfiguration.AMBIENTLIGHT, this.name.name()));
      enableAmbientlightNotification();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.PRESSURE)) {
      this.sampleLoggers.put(SensorConfiguration.PRESSURE, new SampleLogger(this.properties, SensorConfiguration.PRESSURE, this.name.name()));
      enablePressureNotification();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.MOVEMENT)) {
      this.sampleLoggers.put(SensorConfiguration.MOVEMENT, new SampleLogger(this.properties, SensorConfiguration.MOVEMENT, this.name.name()));
      enableMovementNotification();
    }
  }

  @Override
  public void disableNotification() {
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.IRTEMPERATURE)) {
      disableTemperatureNotification();
      this.sampleLoggers.get(SensorConfiguration.IRTEMPERATURE).close();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.HUMIDITY)) {
      disableHumidityNotification();
      this.sampleLoggers.get(SensorConfiguration.HUMIDITY).close();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.AMBIENTLIGHT)) {
      disableAmbientlightNotification();
      this.sampleLoggers.get(SensorConfiguration.AMBIENTLIGHT).close();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.PRESSURE)) {
      disablePressureNotification();
      this.sampleLoggers.get(SensorConfiguration.PRESSURE).close();
    }
    if (this.sensorConfiguration.containsSetting(SensorConfiguration.MOVEMENT)) {
      disableMovementNotification();
      this.sampleLoggers.get(SensorConfiguration.MOVEMENT).close();
    }
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private String getIRtemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16)) >>> 2;

    float objectTemperature = Math.round(raw * 0.03125f * 10) / 10.0f;

    return "Object temperature (IR): " + objectTemperature + "°C";
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private String getDieTemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16)) >>> 2;

    float dieTemperature = Math.round(raw * 0.03125f * 10) / 10.0f;

    return "Die temperature: " + dieTemperature + "°C";
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private String getTemperatureFromBarometricPressureSensor(String data) {
    int raw = Integer.parseInt(data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2), 16);
    float temperature = raw / 100.0f;
    return "Temperature (press): " + temperature + "°C";
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Pressure in hectopascal (hPa)
   */
  private String getPressure(String data) {
    int raw = Integer.parseInt(data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8), 16);
    float pressure = raw / 100.0f;
    return "Pressure: " + pressure + "hPa";
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private String getTemperatureFromHumiditySensor(String data) {
    int raw = Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16);

    float temperature = Math.round((((float) raw / 65536) * 165 - 40) * 10) / 10.0f;

    return "Temperature (hum): " + temperature + "°C";
  }

  /**
   *
   * @param data
   * @return Relative Humidity (%RH)
   */
  private String getRelativeHumidty(String data) {
    int raw = Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16);

    float humidity = Math.round((((float) raw / 65536) * 100) * 10) / 10.0f;

    return "Relative humidity: " + humidity + "%RH";
  }

  /**
   *
   * @param data
   * @return Light intensity in LUX
   */
  private String getAmbientLight(String data) {
    int raw = Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16);

    int m = raw & 0x0FFF;
    int e = (raw & 0xF000) >>> 12;

    float ambientlight = (float) (m * (0.01 * Math.pow(2.0, e)));

    return "Ambientlight: " + ambientlight + "lx";
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

  // TODO split different measurement types from one sensor to different files. Remove duplicated measures like temperature?
  @Override
  public void processSensorData(String handle, String rawHexValues) {
    rawHexValues = rawHexValues.replace(" ", "");
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        this.sampleLoggers.get(SensorConfiguration.IRTEMPERATURE).write(getIRtemperatureFromTemperatureSensor(rawHexValues));
        this.sampleLoggers.get(SensorConfiguration.IRTEMPERATURE).write(getDieTemperatureFromTemperatureSensor(rawHexValues));
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        this.sampleLoggers.get(SensorConfiguration.PRESSURE).write(getTemperatureFromBarometricPressureSensor(rawHexValues));
        this.sampleLoggers.get(SensorConfiguration.PRESSURE).write(getPressure(rawHexValues));
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        this.sampleLoggers.get(SensorConfiguration.AMBIENTLIGHT).write(getAmbientLight(rawHexValues));
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        this.sampleLoggers.get(SensorConfiguration.HUMIDITY).write(getTemperatureFromHumiditySensor(rawHexValues));
        this.sampleLoggers.get(SensorConfiguration.HUMIDITY).write(getRelativeHumidty(rawHexValues));
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        this.sampleLoggers.get(SensorConfiguration.MOVEMENT).write(getRotation(rawHexValues));
        this.sampleLoggers.get(SensorConfiguration.MOVEMENT).write(getAcceleration(rawHexValues));
        this.sampleLoggers.get(SensorConfiguration.MOVEMENT).write(getMagnetism(rawHexValues));
        break;
      default:
        LOG.error("unexpected handle notification " + handle + " : " + rawHexValues);
        break;
    }
  }

}
