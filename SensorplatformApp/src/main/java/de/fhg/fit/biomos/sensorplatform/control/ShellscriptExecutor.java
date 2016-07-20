package de.fhg.fit.biomos.sensorplatform.control;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

/**
 * Execute a simple shell script as sudo to configure a LED on the board. By using different blinking patterns the Sensorplatform displays it's internal state.
 *
 * @author Daniel Pyka
 *
 */
public class ShellscriptExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(ShellscriptExecutor.class);

  public ShellscriptExecutor() {
    // unused
  }

  /**
   * Set a blinking pattern for the LED. "Blink" is standard after booting complete. "Heartbeat" displays the user that the sensorplatform is connected to
   * sensors and is working correctly.
   *
   * @param ledstate
   */
  public static void setLED(LEDstate ledstate, String fileName) {
    try {
      String[] cmd = { "/bin/sh", "-c", "sudo -i sh " + fileName + " " + ledstate.toString() };
      Process p = Runtime.getRuntime().exec(cmd);
      p.waitFor();
      LOG.info("LED state " + ledstate.name());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
