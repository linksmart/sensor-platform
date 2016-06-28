package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public enum SensorType {
  HRM("HRM"), SPO2("SpO2"), GENERALPURPOSE("generalpurpose"), STEPCOUNTER("stepcounter"), UNDEFINED("undefined");

  private final String name;

  private SensorType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
