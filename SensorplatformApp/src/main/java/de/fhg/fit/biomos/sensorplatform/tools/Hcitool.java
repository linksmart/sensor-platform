package de.fhg.fit.biomos.sensorplatform.tools;

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

  public void collectLocalDeviceAndAddress();

  public List<String> scan();

  public void lescan(int scanDuration);

  public List<String> findDevices(int scanDuration);

}
