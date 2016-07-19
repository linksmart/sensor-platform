package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.sensortag.CC2650lib}
 *
 * @author Daniel Pyka
 *
 */
public class CC2650 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650.class);

  public static final String IRTEMPERATURE = "irtemperature";
  public static final String HUMIDITY = "humidity";
  public static final String AMBIENTLIGHT = "ambientlight";
  public static final String PRESSURE = "pressure";
  public static final String MOVEMENT = "movement";

  // Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  public CC2650(SensorName name, String bdAddress, AddressType addressType, String timestampFormat, JSONObject settings) {
    super(name, bdAddress, addressType, timestampFormat, settings);
  }

  /**
   * Enable temperature sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableTemperatureNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + this.settings.getString(IRTEMPERATURE));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + enableNotification);
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
  private void disableTemperatureNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
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
  private void enableHumidityNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + this.settings.getString(HUMIDITY));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + enableNotification);
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
  private void disableHumidityNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + CC2650lib.INTERVAL_HUMIDITY_1000MS_DEFAULT);
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
  private void enableAmbientlightNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + this.settings.getString(AMBIENTLIGHT));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + enableNotification);
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
  private void disableAmbientlightNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + CC2650lib.INTERVAL_AMBIENTLIGHT_800MS_DEFAULT);
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
  private void enablePressureNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + this.settings.getString(PRESSURE));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + enableNotification);
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
  private void disablePressureNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + CC2650lib.INTERVAL_PRESSURE_1000MS_DEFAULT);
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
  private void enableMovementNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + this.settings.getString(MOVEMENT));
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + CC2650lib.VALUE_MOVEMENT_ACTIVATE_ALL_16G);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + enableNotification);
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
  private void disableMovementNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + CC2650lib.VALUE_MOVEMENT_DEACTIVATE_ALL_16G);
      this.bw.newLine();
      this.bw.flush();
      this.bw.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + CC2650lib.INTERVAL_MOVEMENT_1000MS_DEFAULT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable movement notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification(BufferedWriter bw, String charWriteCmd, String enableNotification) {
    this.bw = bw;
    if (this.settings.has(IRTEMPERATURE)) {
      // if (this.fileLogging) {
      // this.sampleLoggers.put(IRTEMPERATURE, new SampleLogger(IRTEMPERATURE, this.name.name()));
      // this.sampleLoggers.get(IRTEMPERATURE).addDescriptionLine("Infrared temperature [" + Unit.DEGREES_CELSIUS + "]");
      // this.sampleLoggers.get(IRTEMPERATURE).addDescriptionLine("Die temperature [" + Unit.DEGREES_CELSIUS + "]");
      // }
      enableTemperatureNotification(charWriteCmd, enableNotification);
    }
    if (this.settings.has(HUMIDITY)) {
      // if (this.fileLogging) {
      // this.sampleLoggers.put(HUMIDITY, new SampleLogger(HUMIDITY, this.name.name()));
      // this.sampleLoggers.get(HUMIDITY).addDescriptionLine("Temperature [" + Unit.DEGREES_CELSIUS + "]");
      // this.sampleLoggers.get(HUMIDITY).addDescriptionLine("Humidity [" + Unit.PERCENT_RELATIVE_HUMIDITY + "]");
      // }
      enableHumidityNotification(charWriteCmd, enableNotification);
    }
    if (this.settings.has(AMBIENTLIGHT)) {
      // if (this.fileLogging) {
      // this.sampleLoggers.put(AMBIENTLIGHT, new SampleLogger(AMBIENTLIGHT, this.name.name()));
      // this.sampleLoggers.get(AMBIENTLIGHT).addDescriptionLine("Ambient light [" + Unit.LUX + "]");
      // }
      enableAmbientlightNotification(charWriteCmd, enableNotification);
    }
    if (this.settings.has(PRESSURE)) {
      // if (this.fileLogging) {
      // this.sampleLoggers.put(PRESSURE, new SampleLogger(PRESSURE, this.name.name()));
      // this.sampleLoggers.get(PRESSURE).addDescriptionLine("Temperature [" + Unit.DEGREES_CELSIUS + "]");
      // this.sampleLoggers.get(PRESSURE).addDescriptionLine("Pressure [" + Unit.HEKTOPASCAL + "]");
      // }
      enablePressureNotification(charWriteCmd, enableNotification);
    }
    if (this.settings.has(MOVEMENT)) {
      // if (this.fileLogging) {
      // this.sampleLoggers.put(MOVEMENT, new SampleLogger(MOVEMENT, this.name.name()));
      // this.sampleLoggers.get(MOVEMENT).addDescriptionLine("Rotation X Y Z [" + Unit.DEGREES_PER_SECOND + "]");
      // this.sampleLoggers.get(MOVEMENT).addDescriptionLine("Acceleration X Y Z [" + Unit.G_FORCE + "]");
      // this.sampleLoggers.get(MOVEMENT).addDescriptionLine("Magnetism X Y Z [" + Unit.MICROTESLA + "]");
      // }
      enableMovementNotification(charWriteCmd, enableNotification);
    }
  }

  @Override
  public void disableNotification(String charWriteCmd, String disableNotification) {
    if (this.settings.has(IRTEMPERATURE)) {
      disableTemperatureNotification(charWriteCmd, disableNotification);
    }
    if (this.settings.has(HUMIDITY)) {
      disableHumidityNotification(charWriteCmd, disableNotification);

    }
    if (this.settings.has(AMBIENTLIGHT)) {
      disableAmbientlightNotification(charWriteCmd, disableNotification);

    }
    if (this.settings.has(PRESSURE)) {
      disablePressureNotification(charWriteCmd, disableNotification);
    }
    if (this.settings.has(MOVEMENT)) {
      disableMovementNotification(charWriteCmd, disableNotification);
    }
    // for (Entry<String, SampleLogger> entry : this.sampleLoggers.entrySet()) {
    // entry.getValue().close();
    // }
    this.bw = null;
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getIRtemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16)) >>> 2;
    return Math.round(raw * 0.03125f * 10) / 10.0f;
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getDieTemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16)) >>> 2;
    return Math.round(raw * 0.03125f * 10) / 10.0f;
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getTemperatureFromBarometricPressureSensor(String data) {
    int raw = Integer.parseInt(data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2), 16);
    return raw / 100.0f;
  }

  /**
   * The conversion and adjustment calculations is done in firmware of the sensor.
   *
   * @param data
   * @return Pressure in hectopascal (hPa)
   */
  private float getPressure(String data) {
    int raw = Integer.parseInt(data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8), 16);
    return raw / 100.0f;
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getTemperatureFromHumiditySensor(String data) {
    int raw = Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16);
    return Math.round((((float) raw / 65536) * 165 - 40) * 10) / 10.0f;
  }

  /**
   *
   * @param data
   * @return Relative Humidity (%RH)
   */
  private float getRelativeHumidty(String data) {
    int raw = Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16);
    return Math.round((((float) raw / 65536) * 100) * 10) / 10.0f;
  }

  /**
   *
   * @param data
   * @return Light intensity in LUX
   */
  private float getAmbientLight(String data) {
    int raw = Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16);
    int m = raw & 0x0FFF;
    int e = (raw & 0xF000) >>> 12;
    return (float) (m * (0.01 * Math.pow(2.0, e)));
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
    return "X:" + rotationX + " Y:" + rotationY + " Z:" + rotationZ;
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
    return "X:" + accX + " Y:" + accY + " Z:" + accZ;
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
    return "X:" + magX + " Y:" + magY + " Z:" + magZ;
  }

  // TODO split different measurement types from one sensor to different files. Remove duplicated measures like temperature?
  // @Override
  // public CC2650Sample getSensorData(String handle, String rawHexData) {
  // CC2650Sample sample = new CC2650Sample(this.dtf.print(new DateTime()));
  // String rawHexValues = rawHexData.replace(" ", "");
  // String timestamp = this.dtf.print(new DateTime());
  // switch (handle) {
  // case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
  // float objectTemperature = getIRtemperatureFromTemperatureSensor(rawHexValues);
  // float dieTemperature = getDieTemperatureFromTemperatureSensor(rawHexValues);
  // // if (this.fileLogging) {
  // // this.sampleLoggers.get(IRTEMPERATURE).write(timestamp, objectTemperature + " " + dieTemperature);
  // // }
  // // if (this.consoleLogging) {
  // // System.out.println(timestamp + " " + objectTemperature + " " + dieTemperature);
  // // }
  // break;
  // case CC2650lib.HANDLE_PRESSURE_VALUE:
  // float temperatureBaro = getTemperatureFromBarometricPressureSensor(rawHexValues);
  // float pressure = getPressure(rawHexValues);
  // // if (this.fileLogging) {
  // // this.sampleLoggers.get(PRESSURE).write(timestamp, temperatureBaro + " " + pressure);
  // // }
  // // if (this.consoleLogging) {
  // // System.out.println(timestamp + " " + temperatureBaro + " " + pressure);
  // // }
  // break;
  // case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
  // float ambientlight = getAmbientLight(rawHexValues);
  // // if (this.fileLogging) {
  // // this.sampleLoggers.get(AMBIENTLIGHT).write(timestamp, Float.toString(ambientlight));
  // // }
  // // if (this.consoleLogging) {
  // // System.out.println(timestamp + " " + ambientlight);
  // // }
  // break;
  // case CC2650lib.HANDLE_HUMIDITY_VALUE:
  // float temperatureHum = getTemperatureFromHumiditySensor(rawHexValues);
  // float humidity = getRelativeHumidty(rawHexValues);
  // // if (this.fileLogging) {
  // // this.sampleLoggers.get(HUMIDITY).write(timestamp, temperatureHum + " " + humidity);
  // // }
  // // if (this.consoleLogging) {
  // // System.out.println(timestamp + " " + temperatureHum + " " + humidity);
  // // }
  // break;
  // case CC2650lib.HANDLE_MOVEMENT_VALUE:
  // String rotation = getRotation(rawHexValues);
  // String acceleration = getAcceleration(rawHexValues);
  // String magnetism = getMagnetism(rawHexValues);
  // // if (this.fileLogging) {
  // // this.sampleLoggers.get(MOVEMENT).write(timestamp, rotation + " " + acceleration + " " + magnetism);
  // // }
  // // if (this.consoleLogging) {
  // // System.out.println(timestamp + " " + rotation + " " + acceleration + " " + magnetism);
  // // }
  // break;
  // default:
  // LOG.error("unexpected handle notification " + handle + " : " + rawHexData);
  // break;
  // }
  // return sample;
  // }

}
