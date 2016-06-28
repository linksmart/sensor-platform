package de.fhg.fit.biomos.sensorplatform.util;

import java.util.HashMap;

/**
 * Contains information about a specific value (e.g. temperature) and it's sampling frequency. Interval is specified in milliseconds.
 *
 * @author Daniel Pyka
 *
 */
public class SensorConfiguration {

  HashMap<String, String> intervals = new HashMap<String, String>();

  public SensorConfiguration() {
    //
  }

  public SensorConfiguration addSetting(String measurementType, String interval) {
    this.intervals.put(measurementType, interval);
    return this;
  }

  public String getSetting(String measurementType) {
    return this.intervals.get(measurementType);
  }

}
