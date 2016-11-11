package de.fhg.fit.biomos.sensorplatform.deprecated;

/**
 * Control class for hciconfig command line tool from Bluez. This one is NOT interactive. Because of strange bugs on the Raspberry Pi 3, the sensorplatform must
 * restart the local Bluetoth controller once. Otherwise pairing is not working properly.
 *
 * @author Daniel Pyka
 *
 */
public interface Hciconfig {

  /**
   * Read the local Bluetooth controller address.
   *
   * @return local Bluetooth address.
   */
  public String getLocalBDaddress();

  /**
   * Start the Bluetooth controller.
   */
  public void up();

  /**
   * Shut down the Bluetooth controller.
   */
  public void down();

}
