package de.fhg.fit.biomos.sensorplatform.main;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.LEDstate;

/**
 * Execute a simple shell script as sudo to configure a LED on the board. By using different blinking patterns the Sensorplatform displays it's internal state.
 *
 * @author Daniel Pyka
 *
 */
public class LEDcontrol {

  private static final Logger LOG = LoggerFactory.getLogger(LEDcontrol.class);

  private final Properties properties;

  public LEDcontrol(Properties properties) {
    this.properties = properties;
  }

  /**
   * Set a blinking pattern for the LED.
   *
   * @param ledstate
   */
  public void setLED(LEDstate ledstate) {
    String filename = this.properties.getProperty("led.control.script");
    try {
      String[] cmd = { "/bin/sh", "-c", "sudo -i sh " + filename + " " + ledstate.toString() };
      Process p = Runtime.getRuntime().exec(cmd);
      p.waitFor();
      LOG.info("LED state " + ledstate.toString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
