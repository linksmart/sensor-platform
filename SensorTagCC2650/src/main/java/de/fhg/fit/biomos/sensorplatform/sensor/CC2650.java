package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;
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

  private static final int ACCELERATION_RESOLUTION = 16;

  public CC2650(SensorName name, String bdAddress, AddressType addressType, JSONObject settings) {
    super(name, bdAddress, addressType, settings);
  }

  /**
   * Enable temperature sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableTemperatureNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification, String period) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + period);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable temperature notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable temperature sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disableTemperatureNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable temperature notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Enable humidity sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableHumidityNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification, String period) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + period);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable humidity notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable humidity sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disableHumidityNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + CC2650lib.INTERVAL_HUMIDITY_1000MS_DEFAULT);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable humidity notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Enable ambientlight sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enableAmbientlightNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification, String period) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + period);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable ambientlight notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable ambientlight sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disableAmbientlightNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_AMBIENTLIGHT_PERIOD + " " + CC2650lib.INTERVAL_AMBIENTLIGHT_800MS_DEFAULT);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable ambientlight notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Enable pressure sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.
   */
  private void enablePressureNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification, String period) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + period);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + CC2650lib.ENABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable pressure notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable pressure sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disablePressureNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_ENABLE + " " + CC2650lib.DISABLE_MEASUREMENT);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_PRESSURE_PERIOD + " " + CC2650lib.INTERVAL_PRESSURE_1000MS_DEFAULT);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable pressure notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Enable movement sensor measurement, notification and set the notification peroid to the value given by the sensor configuration.</br>
   *
   */
  private void enableMovementNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification, String period, String configuration) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + period);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + configuration);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable movement notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable movement sensor measurement, notification and reset the notification peroid to the default value.
   */
  private void disableMovementNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification, String configuration) {
    try {
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_ENABLE + " " + configuration);
      streamToSensor.newLine();
      streamToSensor.flush();
      streamToSensor.write(charWriteCmd + " " + CC2650lib.HANDLE_MOVEMENT_PERIOD + " " + CC2650lib.INTERVAL_MOVEMENT_1000MS_DEFAULT);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable movement notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    if (this.settings.has(IRTEMPERATURE)) {
      enableTemperatureNotification(streamToSensor, charWriteCmd, enableNotification, this.settings.getString(IRTEMPERATURE));
    }
    if (this.settings.has(HUMIDITY)) {
      enableHumidityNotification(streamToSensor, charWriteCmd, enableNotification, this.settings.getString(HUMIDITY));
    }
    if (this.settings.has(AMBIENTLIGHT)) {
      enableAmbientlightNotification(streamToSensor, charWriteCmd, enableNotification, this.settings.getString(AMBIENTLIGHT));
    }
    if (this.settings.has(PRESSURE)) {
      enablePressureNotification(streamToSensor, charWriteCmd, enableNotification, this.settings.getString(PRESSURE));
    }
    if (this.settings.has(MOVEMENT)) {
      // FIXME Currently there are presets defined for the configuration
      // the acceleration range is fixed at -16, +16 G
      // activate all measurements (up to 9 values) at once
      enableMovementNotification(streamToSensor, charWriteCmd, enableNotification, this.settings.getString(MOVEMENT),
          CC2650lib.VALUE_MOVEMENT_ACTIVATE_ALL_16G);
    }
  }

  @Override
  public void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    if (this.settings.has(IRTEMPERATURE)) {
      disableTemperatureNotification(streamToSensor, charWriteCmd, disableNotification);
    }
    if (this.settings.has(HUMIDITY)) {
      disableHumidityNotification(streamToSensor, charWriteCmd, disableNotification);

    }
    if (this.settings.has(AMBIENTLIGHT)) {
      disableAmbientlightNotification(streamToSensor, charWriteCmd, disableNotification);

    }
    if (this.settings.has(PRESSURE)) {
      disablePressureNotification(streamToSensor, charWriteCmd, disableNotification);
    }
    if (this.settings.has(MOVEMENT)) {
      // FIXME static deactivate configuration
      disableMovementNotification(streamToSensor, charWriteCmd, disableNotification, CC2650lib.VALUE_MOVEMENT_DEACTIVATE_ALL_16G);
    }
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getIRtemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16)) >>> 2;
    return raw * 0.03125f;
  }

  /**
   * @param data
   * @return Temperature in degrees Celsius (°C)
   */
  private float getDieTemperatureFromTemperatureSensor(String data) {
    int raw = (Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16)) >>> 2;
    return raw * 0.03125f;
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
    return ((float) raw / 65536) * 165 - 40;
  }

  /**
   *
   * @param data
   * @return Relative Humidity (%RH)
   */
  private float getRelativeHumidty(String data) {
    int raw = Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16);
    return ((float) raw / 65536) * 100;
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
   * Calculate the rotation on the X axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return rotation on the X axis.
   */
  private float getRotationX(String data) {
    return Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16) * 1.0f / (65536 / 500);
  }

  /**
   * Calculate the rotation on the Y axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return rotation on the Y axis.
   */
  private float getRotationY(String data) {
    return Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16) * 1.0f / (65536 / 500);
  }

  /**
   * Calculate the rotation on the Z axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return rotation on the Z axis.
   */
  private float getRotationZ(String data) {
    return Integer.parseInt(data.substring(10, 12) + data.substring(8, 10), 16) * 1.0f / (65536 / 500);
    // legacy TODO compare/test
    // Math.round((Integer.parseInt(data.substring(10, 12) + data.substring(8, 10), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
  }

  /**
   * Calculate the acceleration on the X axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return acceleration on the X axis.
   */
  private float getAccelerationX(String data) {
    return Integer.parseInt(data.substring(14, 16) + data.substring(12, 14), 16) * 1.0f / (32768 / ACCELERATION_RESOLUTION);
  }

  /**
   * Calculate the acceleration on the Y axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return acceleration on the Y axis.
   */
  private float getAccelerationY(String data) {
    return Integer.parseInt(data.substring(18, 20) + data.substring(16, 18), 16) * 1.0f / (32768 / ACCELERATION_RESOLUTION);
    // legacy TODO compare/test
    // return Math.round((Integer.parseInt(data.substring(18, 20) + data.substring(16, 18), 16) * 1.0f) / (32768 / ACCELERATION_RESOLUTION) * 100) / 100.0f;
  }

  /**
   * Calculate the acceleration on the Z axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return acceleration on the Z axis.
   */
  private float getAccelerationZ(String data) {
    return 1.0f * Integer.parseInt(data.substring(22, 24) + data.substring(20, 22), 16) * 1.0f / (32768 / ACCELERATION_RESOLUTION);
  }

  /**
   * Calculate the magnetism on the X axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return magnetism on the X axis.
   */
  private float getMagnetismX(String data) {
    return 1.0f * Integer.parseInt(data.substring(26, 28) + data.substring(24, 26), 16);
  }

  /**
   * Calculate the magnetism on the Y axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return magnetism on the Y axis.
   */
  private float getMagnetismY(String data) {
    return 1.0f * Integer.parseInt(data.substring(30, 32) + data.substring(28, 30), 16);
  }

  /**
   * Calculate the magnetism on the Z axis.
   *
   * @param data
   *          raw sensor notification data without spaces
   * @return magnetism on the Z axis.
   */
  private float getMagnetismZ(String data) {
    return Integer.parseInt(data.substring(34, 36) + data.substring(32, 34), 16);
  }

  public CC2650TemperatureSample calculateTemperatureData(String timestamp, String data) {
    CC2650TemperatureSample temperatureSample = new CC2650TemperatureSample(timestamp, this.bdAddress);
    temperatureSample.setObjectTemperature(getIRtemperatureFromTemperatureSensor(data));
    temperatureSample.setDieTemperature(getDieTemperatureFromTemperatureSensor(data));
    return temperatureSample;
  }

  public CC2650HumiditySample calculateHumidityData(String timestamp, String data) {
    CC2650HumiditySample humiditySample = new CC2650HumiditySample(timestamp, this.bdAddress);
    humiditySample.setTemperature(getTemperatureFromHumiditySensor(data));
    humiditySample.setHumidity(getRelativeHumidty(data));
    return humiditySample;
  }

  public CC2650PressureSample calculatePressureData(String timestamp, String data) {
    CC2650PressureSample pressureSample = new CC2650PressureSample(timestamp, this.bdAddress);
    pressureSample.setTemperature(getTemperatureFromBarometricPressureSensor(data));
    pressureSample.setPressure(getPressure(data));
    return pressureSample;
  }

  public CC2650AmbientlightSample calculateAmbientlightData(String timestamp, String data) {
    CC2650AmbientlightSample ambientlightSample = new CC2650AmbientlightSample(timestamp, this.bdAddress);
    ambientlightSample.setAmbientlight(getAmbientLight(data));
    return ambientlightSample;
  }

  public CC2650MovementSample calculateMovementSample(String timestamp, String data) {
    CC2650MovementSample movementSample = new CC2650MovementSample(timestamp, this.bdAddress);
    movementSample.setRotationX(getRotationX(data));
    movementSample.setRotationY(getRotationY(data));
    movementSample.setRotationZ(getRotationZ(data));
    movementSample.setAccelerationX(getAccelerationX(data));
    movementSample.setAccelerationY(getAccelerationY(data));
    movementSample.setAccelerationZ(getAccelerationZ(data));
    movementSample.setMagnetismX(getMagnetismX(data));
    movementSample.setMagnetismY(getMagnetismY(data));
    movementSample.setMagnetismZ(getMagnetismZ(data));
    return movementSample;
  }

}
