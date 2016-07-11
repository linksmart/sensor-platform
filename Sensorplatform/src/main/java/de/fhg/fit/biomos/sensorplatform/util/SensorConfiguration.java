package de.fhg.fit.biomos.sensorplatform.util;

import java.util.HashMap;

/**
 * Contains information about a specific value (e.g. temperature) and it's sampling frequency. Interval is specified in milliseconds.
 *
 * @author Daniel Pyka
 *
 */
public class SensorConfiguration {

  // key definitions for sensor description json file
  public static final String IRTEMPERATURE = "irtemperature";
  public static final String HUMIDITY = "humidity";
  public static final String AMBIENTLIGHT = "ambientlight";
  public static final String PRESSURE = "pressure";
  public static final String MOVEMENT = "movement";
  public static final String HEARTRATE = "hrm";
  public static final String RRINTERVAL = "rr";
  public static final String ONLINEMODE = "onlinemode";
  public static final String WEBINTERFACE = "webinterface";

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

  public boolean containsSetting(String measurementType) {
    return this.intervals.containsKey(measurementType);
  }

}
