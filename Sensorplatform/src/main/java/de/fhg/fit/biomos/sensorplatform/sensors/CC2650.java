package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.main.Main;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.sensors.SensorCommands}
 *
 * @author Daniel Pyka
 *
 */
public class CC2650 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650.class);

  private static final SimpleDateFormat formatter = new SimpleDateFormat("H:mm:ss:SSS");

  private PrintWriter pw = null;
  private static final File temperatureFile = new File(Main.logDirectory, "temperature.log");

  public CC2650(String name, String bdaddress) {
    super(name, bdaddress);
    try {
      if (temperatureFile.exists()) {
        temperatureFile.delete();
        LOG.info("delete old log file");
      }
      this.pw = new PrintWriter(temperatureFile, "UTF-8");
      this.pw.println("# " + name);
      LOG.info("use log file: " + temperatureFile);
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private void enableTemperatureLogging(String intervalTwoHexCharacters) throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + intervalTwoHexCharacters);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("enable temperature logging");
  }

  private void disableTemperatureLogging() throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_IR_TEMPERATURE_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("disable temperature logging");
  }

  private void enableHumidityLogging(String intervalTwoHexCharacters) throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + intervalTwoHexCharacters);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + GatttoolImpl.ENABLE_MEASUREMENT);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("enable humidity logging");
  }

  private void disableHumidityLogging() throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_PERIOD + " " + CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + CC2650lib.HANDLE_HUMIDITY_ENABLE + " " + GatttoolImpl.DISABLE_MEASUREMENT);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("disable humidity logging");
  }

  @Override
  public void enableLogging() {
    try {
      enableTemperatureLogging(CC2650lib.INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT);
      enableHumidityLogging(CC2650lib.INTERVAL_HUMIDITY_1000_DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void disableLogging() {
    try {
      disableTemperatureLogging();
      disableHumidityLogging();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.pw.flush();
    this.pw.close();
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getIRtemperatureFromTemperatureSensor(String data) {
    float scale = 0.03125F;
    data = data.replace(" ", "");
    String val1 = data.substring(0, 4);
    val1 = (val1 + val1.substring(0, 2)).substring(2, 6);

    int v1 = (Integer.parseInt(val1, 16)) >>> 2;
    float object = Math.round(v1 * scale * 10) / 10.0f;

    return "Object: " + object + "°C";
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getDieTemperatureFromTemperatureSensor(String data) {
    float scale = 0.03125F;
    data = data.replace(" ", "");
    String val2 = data.substring(4, 8);
    val2 = (val2 + val2.substring(0, 2)).substring(2, 6);

    int v2 = (Integer.parseInt(val2, 16)) >>> 2;
    float ambience = Math.round(v2 * scale * 10) / 10.0f;

    return "Die: " + ambience + "°C";
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getTemperatureFromBarometricPressureSensor(String data) {
    return data;
  }

  /**
   *
   * @param data
   * @return Pressure in hectopascal (hPa)
   */
  private String getPressure(String data) {
    return data;
  }

  /**
   *
   * @param data
   * @return Temperature in degrees Celsius °C
   */
  private String getTemperatureFromHumiditySensor(String data) {
    data = data.replace(" ", "");
    String val = data.substring(0, 4);
    val = (val + val.substring(0, 2)).substring(2, 6);

    float temp = Math.round(((((float) Integer.parseInt(val, 16)) / 65536) * 165 - 40) * 10) / 10.0f;

    return "Temperature (hum): " + temp + "°C";
  }

  private String getRelativeHumidty(String data) {
    data = data.replace(" ", "");
    String val = data.substring(4, 8);
    val = (val + val.substring(0, 2)).substring(2, 6);

    float hum = Math.round(((((float) Integer.parseInt(val, 16)) / 65536) * 100) * 10) / 10.0f;

    return "Relative humidity: " + hum;
  }

  /**
   *
   * @param data
   * @return Light intensity in LUX
   */
  private String getLight(String data) {
    return data;
  }

  @Override
  public void processSensorData(String handle, String data) {
    String value;
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getIRtemperatureFromTemperatureSensor(data);
        this.pw.println(value);
        System.out.println(value);
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getDieTemperatureFromTemperatureSensor(data);
        this.pw.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getTemperatureFromBarometricPressureSensor(data);
        System.out.println(value);
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getPressure(data);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getLight(data);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getTemperatureFromHumiditySensor(data);
        System.out.println(value);
        value = formatter.format(Calendar.getInstance().getTime()) + " " + getRelativeHumidty(data);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + data;
        System.out.println(value);
        break;
      default:
        LOG.error("unexpected handle notification " + handle);
        break;
    }
  }

}
