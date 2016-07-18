package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for hcitool command line tool. Use it as singleton!<br />
 * hcitool is used for scanning and configuring bluetooth connections. hcitool is not(!) interactive, fire a command and process returns with text output.
 *
 * @see <a href="http://linux.die.net/man/1/hcitool">http://linux.die.net/man/1/hcitool</a>
 *
 * @author Daniel Pyka
 *
 */
public interface Hcitool {

  /**
   * Stores the Bluetooth device (usually hci0) and the Bluetooth address and print both. May not work properly if the system has two Bluetooth devices.
   *
   * @return
   * @throws IOException
   */
  public void collectLocalDeviceAndAddress() throws IOException;

  /**
   * Search for Bluetooth 2.1 devices for a fixed period of time.
   *
   * @return
   */
  public List<String> scan() throws IOException;

  /**
   * Search for Bluetooth Low Energy devices.
   * 
   * @return
   */
  public List<String> lescan(int duration) throws IOException;

}
