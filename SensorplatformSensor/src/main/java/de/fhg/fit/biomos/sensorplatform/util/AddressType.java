package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Used for gatttool as additional parameter. See gatttool -h
 *
 * @author Daniel Pyka
 *
 */
public enum AddressType {
  PUBLIC("public"), RANDOM("random"), STATIC("random"); // Bluez inconsistency

  private final String type;

  private AddressType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return this.type;
  }
}
