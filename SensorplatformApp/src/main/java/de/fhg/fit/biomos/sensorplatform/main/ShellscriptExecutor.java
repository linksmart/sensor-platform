package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

/**
 * Execute a simple shell script as sudo to configure a LED on the board. By using different blinking patterns the Sensorplatform displays it's internal state.
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 * 
 * @author Daniel Pyka
 *
 */
public class ShellscriptExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(ShellscriptExecutor.class);

  private final String ledControlScriptFileName;

  @Inject
  public ShellscriptExecutor(@Named("led.control.script") String ledControlScriptFileName) {
    this.ledControlScriptFileName = ledControlScriptFileName;
  }

  /**
   * Set a blinking pattern for the LED. "Blink" is standard after booting complete. "Heartbeat" displays the user that the sensorplatform is connected to
   * sensors and is working correctly.
   *
   * @param ledstate
   */
  public void setLED(LEDstate ledstate) {
    try {
      String[] cmd = { "/bin/sh", "-c", "sudo -i sh " + this.ledControlScriptFileName + " " + ledstate.toString() };
      Process p = Runtime.getRuntime().exec(cmd);
      p.waitFor();
      LOG.info("LED state " + ledstate.name());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
