package de.fhg.fit.biomos.sensorplatform.sensor;

import java.util.List;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 * This interface defines methods for interpreting and calculating heart rate data from the Bluetooth specification. The raw heart rate values as hexadecimal
 * contains a configuration byte as prefix. Dependent on the bits set in this byte, the additional values are interpreted accordingly.
 *
 * @author Daniel Pyka
 *
 */
public interface HeartRateSensor {

  /**
   * Check if the bit for 16 bit heart rate value is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if 16 bit, false if 8 bit
   */
  public boolean is16BitHeartRateValue(String rawHexValues);

  /**
   * Check if the bit for rr intervals is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if rr interval data is available, false otherwise
   */
  public boolean isRRintervalDataAvailable(String rawHexValues);

  /**
   * Check if the bit for skin contact detection support is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if skin contact detection support is available, false otherwise
   */
  public boolean isSkinContactDetectionSupported(String rawHexValues);

  /**
   * Check if the bit for skin contact detection is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if skin contact detection is available, false otherwise
   */
  public boolean isSkinContactDetected(String rawHexValues);

  /**
   * Check if the bit for energy expended is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if energy expended is available, false otherwise
   */
  public boolean isEnergyExpendedPresent(String rawHexValues);

  /**
   * Calculate the heart rate (8 bit).
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return int heart rate value
   */
  public int getHeartRate8Bit(String rawHexValues);

  /**
   * Calculate the heart rate (16 bit).
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return int heart rate value
   */
  public int getHeartRate16Bit(String rawHexValues);

  /**
   * Calculate the energy expended value.
   *
   * @param index
   *          depends on the configuration byte
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return int energy expended value
   */
  public int getEnergyExpended(int index, String rawHexValues);

  /**
   * Calculate the RR values as a list
   *
   * @param index
   *          depends on if the input string contains 8 or 16 bit heart rate value
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return List&lt;Float&gt; list of all rr intervals (can be none, one or more)
   */
  public List<Float> getRRintervals(int index, String rawHexValues);

  /**
   * Calculate a HeartRateSample from the given notification data.
   *
   * @param timestamp
   *          the timestamp the sensorplatform and database is working with
   * @param handle
   *          handle address of the notification
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return HeartRateSample for further processing
   */
  public HeartRateSample calculateHeartRateData(String timestamp, String handle, String rawHexValues);

}