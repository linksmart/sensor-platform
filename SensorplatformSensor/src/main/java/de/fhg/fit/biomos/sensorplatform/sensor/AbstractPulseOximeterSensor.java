package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.PulseOximeterGattLibrary;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for pulse oximeter sensors, which are using the GATT characteristics (0x2A37). Every new pulse oximeter sensor, which is compliant with the
 * Bluetooth specification, may inherit from this class. Only a small subset of functions is implemented yet!!
 *
 * @see <a href= "https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.plx_continuous_measurement.xml">Heart Rate
 *      Measurement</a>
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractPulseOximeterSensor extends Sensor<PulseOximeterGattLibrary> implements PulseOximeterSensor {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractPulseOximeterSensor.class);

  public AbstractPulseOximeterSensor(PulseOximeterGattLibrary pulseOximeterGattLibrary, SensorName name, String bdAddress, AddressType addressType,
      SecurityLevel securityLevel, JSONObject settings) {
    super(pulseOximeterGattLibrary, name, bdAddress, addressType, securityLevel, settings);
  }

  @Override
  public boolean hasSpO2PRfastField(String rawHexValues) {
    LOG.error("not yet implemented");
    return false;
  }

  @Override
  public boolean hasSpO2PRslowField(String rawHexValues) {
    LOG.error("not yet implemented");
    return false;
  }

  @Override
  public boolean hasMeasurementStatusField(String rawHexValues) {
    LOG.error("not yet implemented");
    return false;
  }

  @Override
  public boolean hasDeviceAndSensorStatusField(String rawHexValues) {
    LOG.error("not yet implemented");
    return false;
  }

  @Override
  public boolean hasPulseAmplitudeIndexField(String rawHexValues) {
    LOG.error("not yet implemented");
    return false;
  }

  @Override
  public int getSpO2PRnormalSpO2(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(3, 8).replace(" ", ""), 16);
  }

  @Override
  public int getSpO2PRnormalPulseRate(String rawHexValues) {
    return Integer.parseInt(rawHexValues.substring(9, 14).replace(" ", ""), 16);
  }

  @Override
  public int getSpO2PRfastSpO2(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public int getSpO2PRfastPulseRate(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public int getSpO2PRslowSpO2(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public int getSpO2PRslowPulseRate(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public short getMeasurementStatus(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public short getDeviceAndSensorStatus(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public short getPulseAmplitudeIndex(String rawHexValues) {
    LOG.error("not yet implemented");
    return 0;
  }

  @Override
  public PulseOximeterSample calculatePulseOximeterData(String timestamp, String handle, String rawHexValues) {
    if (!handle.equals(this.gattLibrary.getHandlePulseOximeterMeasurement())) {
      LOG.warn("unexpected handle address {} {}", handle, rawHexValues);
      return null;
    }

    PulseOximeterSample sample = new PulseOximeterSample(timestamp, this.bdAddress);
    sample.setPulseRate(getSpO2PRnormalPulseRate(rawHexValues));
    sample.setSpO2(getSpO2PRnormalSpO2(rawHexValues));
    return sample;
  }

}
