package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public class InvalidSensorDataException extends Exception {
  private static final long serialVersionUID = -2871499328255678076L;

  public InvalidSensorDataException() {
    super();
  }

  public InvalidSensorDataException(String message) {
    super(message);
  }

  public InvalidSensorDataException(Throwable cause) {
    super(cause);
  }

  public InvalidSensorDataException(String message, Throwable cause) {
    super(message, cause);
  }

}
