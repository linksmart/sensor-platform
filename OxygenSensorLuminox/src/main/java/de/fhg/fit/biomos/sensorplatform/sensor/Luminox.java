package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import de.fhg.fit.biomos.sensorplatform.gatt.Luminoxlib;
import de.fhg.fit.biomos.sensorplatform.sample.LuminoxAirPressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.LuminoxOxygenSample;
import de.fhg.fit.biomos.sensorplatform.sample.LuminoxTemperatureSample;
import de.fhg.fit.biomos.sensorplatform.util.FloatUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 *
 * @author Daniel Pyka
 *
 */
public class Luminox extends AbstractSensor<Luminoxlib> {

  private static final Logger LOG = LoggerFactory.getLogger(Luminox.class);

  private static final String TEMPERATURE = "temperature";
  private static final String PRESSURE = "pressure";
  private static final String OXYGEN = "oxygen";




  public Luminox(SensorName name, String bdaddress, JSONObject settings) {
    super(new Luminoxlib(), name, bdaddress, AddressType.PUBLIC, SecurityLevel.LOW, settings);
  }


  /**
   * Enable pulse oximeter notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag, only the notification. This sensor measures normal SpO2 and pulse rate values.
   *
   * @param streamToSensor
   *          the stream to the gatttool
   * @param charWriteCmd
   *          the gatttool command for writing to a handle
   * @param enableNotification
   *          the bitmask for enabling notifications
   */
  private void enableLuminoxNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandleLuminoxNotification() + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable Luminox notification");
    } catch (IOException e) {
      LOG.error("cannot enable Luminox notification", e);
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   *
   * @param streamToSensor
   *          the stream to the gatttool
   * @param charWriteCmd
   *          the gatttool command for writing to a handle
   * @param disableNotification
   *          the bitmask for disabling notifications
   */
  private void disableLuminoxNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandleLuminoxNotification() + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable Luminox notification");
    } catch (IOException e) {
      LOG.error("cannot disable Luminox notification", e);
    }
  }


  @Override
  public void enableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enableLuminoxNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    disableLuminoxNotification(streamToSensor, charWriteCmd, disableNotification);
  }


  /**
   * Calculate the oxygen concentration in percentage.
   *
   * @param data
   *          hexadecimal values without whitespaces
   * @return oxygen concentration in percentage
   */
  private float getOxygenConcentrationPercentage(String data) {
    //return 20.60f;
    return Integer.parseInt(FloatUtils.fromHexString(data).substring(0,5))*0.01f;

    //return FloatUtils.parseSFLOATtoFloat((int)Long.parseLong(data.substring(0,5),16));
    //return 1.0f * (short) Integer.parseInt(data.substring(34, 36) + data.substring(32, 34), 16);
  }

  /**
   * Calculate the oxygen concentration in ppO2.
   *
   * @param data
   *          hexadecimal values without whitespaces
   * @return oxygen concentration in percentage
   */
  private float getOxygenConcentrationppO2(String data) {
    return 20.8f;
    //return (int)Long.parseLong(data.substring(12,16).replace(".",""))*0.01f;
  }


  /**
   * Calculate the Temperature
   *
   * @param data
   *          hexadecimal values without whitespaces
   * @return oxygen concentration in percentage
   */
  private float getTemperature(String data) {
    //return FloatUtils.parseSFLOATtoFloat((int)Long.parseLong(data.substring(8,16),16));
    return Integer.parseInt(FloatUtils.fromHexString(data).substring(6,9))*0.1f;
    //return 23.4f;
  }

  /**
   * Calculate the barometric pressure
   *
   * @param data
   *          hexadecimal values without whitespaces
   * @return oxygen concentration in percentage
   */
  private float getAirPressure(String data) {
    //return FloatUtils.parseSFLOATtoFloat((int)Long.parseLong(data.substring(16,24),16));
    return Integer.parseInt(FloatUtils.fromHexString(data).substring(10,14))*1f;
    //return 1010f;
  }

  /**
   * Calculate the sensos
   *
   * @param data
   *          hexadecimal values without whitespaces
   * @return oxygen concentration in percentage
   */
  private float getSensorStatus(String data) {
    return (int)Long.parseLong(data.substring(12,16).replace(".",""));
  }

  /**
   * Calculate a temperature sample for the given notification data.
   *
   * @param timestamp
   *          the current timestamp from the sensorwrapper
   * @param data
   *          hexadecimal values without whitespaces
   * @param handle
   *          handle of the notification
   * @return CC2650TemperatureSample for further processing
   */
  public LuminoxTemperatureSample calculateTemperatureData(String timestamp, String handle, String data) {
    if (!handle.equals(this.gattLibrary.HANDLE_LUMINOX_VALUE)) {
      return null;
    }
    LuminoxTemperatureSample temperatureSample = new LuminoxTemperatureSample(timestamp, this.bdAddress, false);
    temperatureSample.setTemperature(getTemperature(data));
    return temperatureSample;
  }

  /**
   * Calculate an oxygen sample for the given notification data.
   *
   * @param timestamp
   *          the current timestamp from the sensorwrapper
   * @param data
   *          hexadecimal values without whitespaces
   * @param handle
   *          handle of the notification
   * @return CC2650TemperatureSample for further processing
   */
  public LuminoxOxygenSample calculateOxygenData(String timestamp, String handle, String data) {
    if (!handle.equals(this.gattLibrary.HANDLE_LUMINOX_VALUE)) {
      return null;
    }
    LuminoxOxygenSample oxygenSample = new LuminoxOxygenSample(timestamp, this.bdAddress, false);
    oxygenSample.setOxygenConcentrationPercent(getOxygenConcentrationPercentage(data));
    oxygenSample.setOxygenConcentrationppO2(getOxygenConcentrationppO2(data));
    return oxygenSample;
  }

  /**
   * Calculate a pressure sample for the given notification data.
   *
   * @param timestamp
   *          the current timestamp from the sensorwrapper
   * @param data
   *          hexadecimal values without whitespaces
   * @param handle
   *          handle of the notification
   * @return CC2650TemperatureSample for further processing
   */
  public LuminoxAirPressureSample calculateAirPressureData(String timestamp, String handle, String data) {
    if (!handle.equals(this.gattLibrary.HANDLE_LUMINOX_VALUE)) {
      return null;
    }
    LuminoxAirPressureSample airPressureSample = new LuminoxAirPressureSample(timestamp, this.bdAddress, false);
    airPressureSample.setPressure(getAirPressure(data));
    return airPressureSample;
  }

}
