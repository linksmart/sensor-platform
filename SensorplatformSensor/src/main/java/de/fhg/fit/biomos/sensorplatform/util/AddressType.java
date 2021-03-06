package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Used for gatttool as additional parameter. See gatttool -h. There is another inconsistency: gatttool does not know a address type static, so random is used
 * in this case, public would not work.
 *
 * @author Daniel Pyka
 *
 */
public enum AddressType {
  PUBLIC("public"), RANDOM("random"), STATIC("random"); // Bluez gatttool inconsistency

  private final String type;

  private AddressType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return this.type;
  }
}
