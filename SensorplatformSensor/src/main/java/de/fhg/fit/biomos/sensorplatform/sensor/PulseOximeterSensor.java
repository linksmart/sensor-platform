package de.fhg.fit.biomos.sensorplatform.sensor;

import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

/**
 * This interface defines methods for interpreting and calculating pulse oximeter data from the Bluetooth specification. The raw pulse oximeter values as
 * hexadecimal contains a configuration byte as prefix. Dependent on the bits set in this byte, the additional values are interpreted accordingly.
 *
 * @author Daniel Pyka
 *
 */
public interface PulseOximeterSensor {

  /**
   * Check if the bit for SpO2PRfast is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the bit is set, false otherwise
   */
  public boolean hasSpO2PRfastField(String rawHexValues);

  /**
   * Check if the bit for SpO2PRslow is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the bit is set, false otherwise
   */
  public boolean hasSpO2PRslowField(String rawHexValues);

  /**
   * Check if the bit for measurement status is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the bit is set, false otherwise
   */
  public boolean hasMeasurementStatusField(String rawHexValues);

  /**
   * Check if the bit for device and sensor status field is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the bit is set, false otherwise
   */
  public boolean hasDeviceAndSensorStatusField(String rawHexValues);

  /**
   * Check if the bit for pulse amplitude index is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the bit is set, false otherwise
   */
  public boolean hasPulseAmplitudeIndexField(String rawHexValues);

  /**
   * Calculate the SpO2 value of the SpO2PRnormal SpO2 field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRnormal SpO2 field
   */
  public int getSpO2PRnormalSpO2(String rawHexValues);

  /**
   * Calculate the pulse rate of the SpO2PRnormal pulse rate field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRnormal pulse rate field
   */
  public int getSpO2PRnormalPulseRate(String rawHexValues);

  /**
   * Calculate the SpO2 value of the SpO2PRfast SpO2 field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRfast SpO2 field
   */
  public int getSpO2PRfastSpO2(String rawHexValues);

  /**
   * Calculate the pulse rate of the SpO2PRfast pulse rate field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRfast pulse rate field
   */
  public int getSpO2PRfastPulseRate(String rawHexValues);

  /**
   * Calculate the SpO2 value of the SpO2PRslow SpO2 field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRslow SpO2 field
   */
  public int getSpO2PRslowSpO2(String rawHexValues);

  /**
   * Calculate the pulse rate of the SpO2PRslow pulse rate field.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of the SpO2PRslow pulse rate field
   */
  public int getSpO2PRslowPulseRate(String rawHexValues);

  /**
   * Calculate the value of measurement status.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return bitmask value of pulse amplitude index
   */
  public short getMeasurementStatus(String rawHexValues);

  /**
   * Calculate the value of device and sensor status.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return bitmask value of device and sensor status
   */
  public short getDeviceAndSensorStatus(String rawHexValues);

  /**
   * Calculate the pulse amplitude index value.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return value of pulse amplitude index (percent)
   */
  public short getPulseAmplitudeIndex(String rawHexValues);

  /**
   * Calculate a PulseOximeterSample from the given notification data.
   *
   * @param timestamp
   *          the timestamp the sensorplatform and database is working with
   * @param handle
   *          handle address of the notification
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return PulseOximeterSample for further processing
   */
  public PulseOximeterSample calculatePulseOximeterData(String timestamp, String handle, String rawHexValues);

}