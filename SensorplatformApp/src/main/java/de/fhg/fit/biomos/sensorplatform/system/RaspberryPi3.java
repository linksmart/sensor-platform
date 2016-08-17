package de.fhg.fit.biomos.sensorplatform.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaspberryPi3 implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(RaspberryPi3.class);

  private static final String LED0_TRIGGER = "/sys/class/leds/led0/trigger";
  private static final String LED0_DELAY_ON = "/sys/class/leds/led0/delay_on";
  private static final String LED0_DELAY_OFF = "/sys/class/leds/led0/delay_off";

  private static final String BLINK_INTERVAL = "1000";

  private enum LEDstate {
    STANDBY("timer"), RECORDING("heartbeat"), ERROR("none");

    private final String state;

    private LEDstate(String state) {
      this.state = state;
    }

    @Override
    public String toString() {
      return this.state;
    }

  }

  public RaspberryPi3() {
    //
  }

  @Override
  public void setLEDstateSTANDBY() {
    try {
      PrintWriter pw = new PrintWriter(new File(LED0_TRIGGER));
      pw.write(LEDstate.STANDBY.toString());
      pw.close();
      pw = new PrintWriter(new File(LED0_DELAY_ON));
      pw.write(BLINK_INTERVAL);
      pw.close();
      pw = new PrintWriter(new File(LED0_DELAY_OFF));
      pw.write(BLINK_INTERVAL);
      pw.close();
      LOG.info("LED standby");
    } catch (FileNotFoundException e) {
      LOG.error("cannot write to board LED file (no sudo)", e);
    }
  }

  @Override
  public void setLEDstateRECORDING() {
    try {
      PrintWriter pw = new PrintWriter(new File(LED0_TRIGGER));
      pw.write(LEDstate.RECORDING.toString());
      pw.close();
      LOG.info("LED recording");
    } catch (FileNotFoundException e) {
      LOG.error("cannot write to board LED file (no sudo)", e);
    }
  }

  @Override
  public void setLEDstateERROR() {
    try {
      PrintWriter pw = new PrintWriter(new File(LED0_TRIGGER));
      pw.write(LEDstate.ERROR.toString());
      pw.close();
      LOG.info("LED error");
    } catch (FileNotFoundException e) {
      LOG.error("cannot write to board LED file (no sudo)", e);
    }
  }

}
