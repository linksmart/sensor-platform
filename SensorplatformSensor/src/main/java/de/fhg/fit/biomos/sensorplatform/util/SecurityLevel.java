package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Security level used by gatttool. Security levels are defined differently in multiple contexts.<br>
 * low: no encryption, no authentication<br>
 * medium: encrpytion, no authentication<br>
 * high: encryption and authentication<br>
 * There is one more possiblity from the bluetooth specification: no encryption but authentication. I do not know how this is represented in Bluez.
 *
 * @author Daniel Pyka
 *
 */
public enum SecurityLevel {
  LOW("low"), MEDIUM("medium"), HIGH("high");

  private final String level;

  private SecurityLevel(String level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return this.level;
  }
}
