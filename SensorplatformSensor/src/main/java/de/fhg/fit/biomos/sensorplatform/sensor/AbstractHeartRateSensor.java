package de.fhg.fit.biomos.sensorplatform.sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.HeartRateGattLibrary;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for heart rate sensors, which are using the GATT characteristics (0x2A37). Every new heart rate sensor, which is compliant with the Bluetooth
 * specification, may inherit from this class.
 *
 * @see <a href=
 *      "https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml">Heart
 *      Rate Measurement</a>
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractHeartRateSensor extends AbstractSensor<HeartRateGattLibrary> implements HeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractHeartRateSensor.class);

  private static final byte UINT16 = 1;
  private static final byte SKIN_CONTACT_DETECTED = 1 << 1;
  private static final byte SKIN_CONTACT_SUPPORTED = 1 << 2;
  private static final byte ENERGY_EXPENDED_PRESENT = 1 << 3;
  private static final byte RR_INTERVAL_AVAILABLE = 1 << 4;

  private static final Pattern PATTERN_RR = Pattern.compile("(\\w{2}\\s\\w{2})+");

  public AbstractHeartRateSensor(HeartRateGattLibrary heartRateGattLibrary, SensorName name, String bdAddress, AddressType addressType,
      SecurityLevel securityLevel, JSONObject sensorConfiguration) {
    super(heartRateGattLibrary, name, bdAddress, addressType, securityLevel, sensorConfiguration);
  }

  @Override
  public boolean is16BitHeartRateValue(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & UINT16) == UINT16) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isRRintervalDataAvailable(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & RR_INTERVAL_AVAILABLE) == RR_INTERVAL_AVAILABLE) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isSkinContactDetectionSupported(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & SKIN_CONTACT_SUPPORTED) == SKIN_CONTACT_SUPPORTED) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isSkinContactDetected(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & SKIN_CONTACT_DETECTED) == SKIN_CONTACT_DETECTED) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isEnergyExpendedPresent(String rawHexValues) {
    byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
    if ((config & ENERGY_EXPENDED_PRESENT) == ENERGY_EXPENDED_PRESENT) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int getEnergyExpended(int index, String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(index + 3, index + 5) + rawHexValues.substring(index, index + 2), 16);
  }

  @Override
  public int getHeartRate8Bit(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(3, 5), 16);
  }

  @Override
  public int getHeartRate16Bit(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(6, 8) + rawHexValues.substring(3, 5), 16);
  }

  @Override
  public List<Float> getRRintervals(int index, String rawHexValues) {
    List<Float> rrIntervals = new ArrayList<>();
    String rrIntervalsHex = rawHexValues.substring(index);

    Matcher m = PATTERN_RR.matcher(rrIntervalsHex);
    while (m.find()) {
      String tmp = m.group(0);
      rrIntervals.add(((float) Integer.parseInt(tmp.substring(3, 5) + tmp.substring(0, 2), 16) / 1024) * 1000);
    }

    return rrIntervals;
  }

  @Override
  public HeartRateSample calculateHeartRateData(String timestamp, String handle, String rawHexValues) {
    if (!handle.equals(this.gattLibrary.getHandleHeartRateMeasurement())) {
      LOG.warn("unexpected handle address {} {}", handle, rawHexValues);
      return null;
    }

    HeartRateSample hrs = new HeartRateSample(timestamp, this.bdAddress, false);

    if (isSkinContactDetectionSupported(rawHexValues)) {
      if (!isSkinContactDetected(rawHexValues)) {
        LOG.warn("No skin contact detected");
      }
    }

    if (is16BitHeartRateValue(rawHexValues)) {
      hrs.setHeartRate(getHeartRate16Bit(rawHexValues));
      if (isEnergyExpendedPresent(rawHexValues)) {
        hrs.setEnergyExpended(getEnergyExpended(9, rawHexValues));
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRintervals(getRRintervals(15, rawHexValues));
          // CC HH HH EE EE RR RR RR RR
        } else {
          hrs.setRRintervals("[]");
          // CC HH HH EE EE
        }
      } else {
        hrs.setEnergyExpended(0);
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRintervals(getRRintervals(9, rawHexValues));
          // CC HH HH RR RR RR RR
        } else {
          hrs.setRRintervals("[]");
          // CC HH HH
        }
      }
    } else {
      hrs.setHeartRate(getHeartRate8Bit(rawHexValues));
      if (isEnergyExpendedPresent(rawHexValues)) {
        hrs.setEnergyExpended(getEnergyExpended(6, rawHexValues));
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRintervals(getRRintervals(12, rawHexValues));
          // CC HH EE EE RR RR RR RR
        } else {
          hrs.setRRintervals("[]");
          // CC HH EE EE
        }
      } else {
        hrs.setEnergyExpended(0);
        if (isRRintervalDataAvailable(rawHexValues)) {
          hrs.setRRintervals(getRRintervals(6, rawHexValues));
          // CC HH RR RR RR RR
        } else {
          hrs.setRRintervals("[]");
          // CC HH
        }
      }
    }

    return hrs;
  }

}
