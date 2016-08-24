package de.fhg.fit.biomos.sensorplatform.sensors;

import java.util.List;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

public interface HeartRateSensor {

  public boolean is16BitValue(String rawHexValues);

  public boolean is8BitValue(String rawHexValues);

  public boolean isRRintervalDataAvailable(String rawHexValues);

  public boolean isSkinContactDetectionSupported(String rawHexValues);

  public boolean isSkinContactDetected(String rawHexValues);

  public boolean isEnergyExpendedSupported(String rawHexValues);

  public int getHeartRate8Bit(String rawHexValues);

  public int getHeartRate16Bit(String rawHexValues);

  public List<Integer> getRRintervalsWith8BitHeartRateData(String rawHexValues);

  public List<Integer> getRRintervalsWith16BitHeartRateData(String rawHexValues);

  public List<Integer> getRRintervals(int index, String rawHexValues);

  public HeartRateSample calculateHeartRateData(String timestamp, String rawHexValues);

}