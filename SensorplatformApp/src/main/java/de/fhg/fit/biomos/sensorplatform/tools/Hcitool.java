package de.fhg.fit.biomos.sensorplatform.tools;

import java.util.List;

import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

/**
 * Control class for hcitool command line tool from Bluez. The Sensorplatform uses this program only for scanning for Bluetooth Low Energy devices.
 *
 * @author Daniel Pyka
 *
 */
public interface Hcitool {

  /**
   * Scan for Bluetooth Low Energy
   *
   * @param duration
   *          scan duration in seconds
   * @return List of available Bluetooth Low Energy devices
   */
  public List<DetectedDevice> scan(int duration);

}
