package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public class BluetoothGattException extends Exception {
  private static final long serialVersionUID = 6343466846595254268L;

  public BluetoothGattException() {
    super();
  }

  public BluetoothGattException(String message) {
    super(message);
  }

  public BluetoothGattException(Throwable cause) {
    super(cause);
  }

  public BluetoothGattException(String message, Throwable cause) {
    super(message, cause);
  }

}
