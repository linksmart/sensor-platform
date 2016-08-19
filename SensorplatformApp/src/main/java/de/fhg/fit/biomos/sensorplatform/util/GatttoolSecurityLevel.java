package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public enum GatttoolSecurityLevel {
  LOW("low"), MEDIUM("medium"), HIGH("high");

  private final String level;

  private GatttoolSecurityLevel(String level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return this.level;
  }
}
