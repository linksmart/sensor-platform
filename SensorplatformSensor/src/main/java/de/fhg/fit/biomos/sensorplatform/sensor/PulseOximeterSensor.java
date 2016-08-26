package de.fhg.fit.biomos.sensorplatform.sensor;

import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

public interface PulseOximeterSensor {

  public boolean hasSpO2PRfastField(String rawHexValues);

  public boolean hasSpO2PRslowField(String rawHexValues);

  public boolean hasMeasurementStatusField(String rawHexValues);

  public boolean hasDeviceAndSensorStatusField(String rawHexValues);

  public boolean hasPulseAmplitudeIndexField(String rawHexValues);

  public int getSpO2PRnormalSpO2(String rawHexValues);

  public int getSpO2PRnormalPulseRate(String rawHexValues);

  public int getSpO2PRfastSpO2(String rawHexValues);

  public int getSpO2PRfastPulseRate(String rawHexValues);

  public int getSpO2PRslowSpO2(String rawHexValues);

  public int getSpO2PRslowPulseRate(String rawHexValues);

  public short getMeasurementStatus(String rawHexValues);

  public short getDeviceAndSensorStatus(String rawHexValues);

  public short getPulseAmplitudeIndex(String rawHexValues);

  public PulseOximeterSample calculatePulseOximeterData(String timestamp, String rawHexValues);

}