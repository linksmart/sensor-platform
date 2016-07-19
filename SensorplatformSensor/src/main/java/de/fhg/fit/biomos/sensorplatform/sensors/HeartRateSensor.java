package de.fhg.fit.biomos.sensorplatform.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.InvalidSensorDataException;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for heart rate sensors, which are using the GATT characteristics (0x2A37). The raw heart rate values as hexadecimal contains a configuration byte
 * as prefix. Dependent on the bits set in this byte, the additional values are interpreted accordingly. The purpose of this class is to provide methods for
 * checking the configuration byte and the conversion of the hexadecimal data to readable values.
 *
 * @see <a href=
 *      "https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml">Heart
 *      Rate Measurement</a>
 *
 * @author Daniel Pyka
 *
 */
public abstract class HeartRateSensor extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(HeartRateSensor.class);

  private static final byte UINT8 = 0;
  private static final byte UINT16 = 1;
  private static final byte SKIN_CONTACT_DETECTED = 1 << 1;
  private static final byte SKIN_CONTACT_SUPPORTED = 1 << 2;
  private static final byte ENERGY_EXPENDED = 1 << 3;
  private static final byte RR_INTERVAL_AVAILABLE = 1 << 4;

  private static final Pattern PATTERN_RR = Pattern.compile("(\\w{2}\\s\\w{2})+");

  public HeartRateSensor(SensorName name, String bdAddress, AddressType addressType, String timestampFormat, JSONObject sensorConfiguration) {
    super(name, bdAddress, addressType, timestampFormat, sensorConfiguration);
  }

  /**
   * Check if the bit for 16 bit heart rate value is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if 16 bit, false if 8 bit
   */
  protected boolean is16BitValue(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & UINT16) == UINT16) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the bit for 16 bit heart rate value is NOT set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if 8 bit, false if 16 bit
   */
  protected boolean is8BitValue(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & UINT8) == UINT8) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the bit for rr intervals is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if the rr in
   */
  protected boolean isRRintervalDataAvailable(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & RR_INTERVAL_AVAILABLE) == RR_INTERVAL_AVAILABLE) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the bit for skin contact detection support is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if skin contact detection support is available, false otherwise
   */
  protected boolean isSkinContactDetectionSupported(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & SKIN_CONTACT_SUPPORTED) == SKIN_CONTACT_SUPPORTED) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the bit for skin contact detection is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if skin contact detection is available, false otherwise
   */
  protected boolean isSkinContactDetected(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & SKIN_CONTACT_DETECTED) == SKIN_CONTACT_DETECTED) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the bit for energy expended is set in the configuration byte.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return true if energy expended is available, false otherwise
   */
  protected boolean isEnergyExpendedSupported(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & ENERGY_EXPENDED) == ENERGY_EXPENDED) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Calculate the heart rate (8 bit).
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return int heart rate
   */
  protected int getHeartRate8Bit(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(3, 5), 16);
  }

  /**
   * Calculate the heart rate (16 bit).
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return int heart rate
   */
  protected int getHeartRate16Bit(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(3, 8).replace(" ", ""), 16);
  }

  /**
   * Calculate the rr interval(s) for a given input string with 8 bit heart rate value.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return List&lt;Integer&gt; list of all rr intervals (can be none, one or more)
   * @throws InvalidSensorDataException
   *           if the input string does not have the expected format
   */
  protected List<Integer> getRRintervalsWith8BitHeartRateData(String rawHexValues) {
    return getRRintervals(6, rawHexValues);
  }

  /**
   * Calculate the rr interval(s) for a given input string with 16 bit heart rate value.
   *
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return List&lt;Integer&gt; list of all rr intervals (can be none, one or more)
   * @throws InvalidSensorDataException
   *           if the input string does not have the expected format
   */
  protected List<Integer> getRRintervalsWith16BitHeartRateData(String rawHexValues) {
    return getRRintervals(9, rawHexValues);
  }

  /**
   * This method should not be called directly!
   *
   * @param index
   *          depends if the input string contains 8 or 16 bit heart rate value
   * @param rawHexValues
   *          raw notification data as hexadecimal from the sensor
   * @return List&lt;Integer&gt; list of all rr intervals (can be none, one or more)
   * @throws InvalidSensorDataException
   *           if the input string does not have the expected format
   */
  protected List<Integer> getRRintervals(int index, String rawHexValues) {
    List<Integer> rrIntervals = new ArrayList<Integer>();
    try {
      String rrIntervalsHex = rawHexValues.substring(index);

      Matcher m = PATTERN_RR.matcher(rrIntervalsHex);
      while (m.find()) {
        String tmp = m.group(0);
        rrIntervals.add(Integer.parseInt(tmp.substring(3, 5) + tmp.substring(0, 2), 16));
      }
    } catch (IndexOutOfBoundsException e) {
      LOG.error("Bad sensor data! Cannot calculate rr intervals.");
    }
    return rrIntervals;
  }

  protected HeartRateSample calculateHeartRateData(String expectedHandle, String handle, String rawHexValues) {
    if (handle.equals(expectedHandle)) {
      HeartRateSample hrs = new HeartRateSample(this.dtf.print(new DateTime()));

      if (isSkinContactDetectionSupported(rawHexValues)) {
        if (!isSkinContactDetected(rawHexValues)) {
          LOG.warn("No skin contact detected");
        }
      }

      if (is8BitValue(rawHexValues)) {
        hrs.setHeartRate(getHeartRate8Bit(rawHexValues));
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRinterval(getRRintervalsWith8BitHeartRateData(rawHexValues));
        }
      } else {
        hrs.setHeartRate(getHeartRate16Bit(rawHexValues));
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRinterval(getRRintervalsWith16BitHeartRateData(rawHexValues));
        }
      }
      return hrs;
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
      return null;
    }
  }
}
