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
  private static final File log = new File("temperature.log");

  public CC2650(String name, String bdaddress) {
    super(name, bdaddress);
    try {
      if (log.exists()) {
        log.delete();
        LOG.info("delete old log file");
      }
      this.pw = new PrintWriter(log, "UTF-8");
      this.pw.println("# " + name);
      LOG.info("use log file: " + log);
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

  private String convertToCelsius(String data) {
    float scale = 0.03125F;
    data = data.replace(" ", ""); // 54 0a c8 0d -> 540ac80d
    String val1 = data.substring(0, 4);
    val1 = (val1 + val1.substring(0, 2)).substring(2, 6); // 540a54 -> 0a54
    String val2 = data.substring(4, 8);
    val2 = (val2 + val2.substring(0, 2)).substring(2, 6); // c80dc8 -> 0dc8

    int v1 = (Integer.parseInt(val1, 16)) >>> 2;
    int v2 = (Integer.parseInt(val2, 16)) >>> 2;

    double object = Math.round(v1 * scale * 10) / 10.0;
    double ambience = Math.round(v2 * scale * 10) / 10.0;
    return "Object: " + object + "°C " + "Die: " + ambience + "°C";
  }

  @Override
  public void processSensorData(String handle, String data) {
    String value;
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + convertToCelsius(data);
        this.pw.println(value);
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + data;
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + data;
        System.out.println(value);
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        value = formatter.format(Calendar.getInstance().getTime()) + " " + data;
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
