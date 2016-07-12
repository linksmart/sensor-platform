package de.fhg.fit.biomos.sensorplatform.util;

import java.util.HashMap;

/**
 * Contains information about:<br>
 * <b>1)</b> A specific measurement (e.g. heart rate) and it's notification period. Interval is specified in milliseconds (hexadecimal!).<br>
 * <b>2)</b> Online mode: Specifies if the sensor should upload it's data to a webinterface and specifies which webinterface.
 *
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
    // default
  }

  /**
   * Adds a pair of measurement and notification peroid to the internal hashmap.
   *
   * @param measurementType
   *          e.g. irtemperature
   * @param interval
   *          e.g. 64 (hexadecimal)
   */
  public void addSetting(String measurement, String notificationPeroid) {
    this.intervals.put(measurement, notificationPeroid);
  }

  /**
   * Get the notification period for a specific measurement.
   *
   * @param measurementType
   * @return String hexadecimal value in milliseconds.
   */
  public String getSetting(String measurement) {
    return this.intervals.get(measurement);
  }

  /**
   * Checks if the interna map contains a specific measurement as key.
   *
   * @param measurement
   *          e.g irtemperature
   * @return true if the map contains the key, false otherwise.
   */
  public boolean containsSetting(String measurement) {
    return this.intervals.containsKey(measurement);
  }

}
