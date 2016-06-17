package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public enum SensorType {
  HRM("heartrate"), SPO2("spo2"), MULTI("multi"), STEPCOUNTER("stepcounter"), UNDEFINED("undefined");

  private final String name;

  private SensorType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
