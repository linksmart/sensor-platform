package de.fhg.fit.biomos.sensorplatform.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaspberryPi3 implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(RaspberryPi3.class);

  private static final String LED0_TRIGGER = "/sys/class/leds/led0/trigger";
  private static final String LED0_DELAY_ON = "/sys/class/leds/led0/delay_on";
  private static final String LED0_DELAY_OFF = "/sys/class/leds/led0/delay_off";
  private static final String BLINK_INTERVAL = "1000";

  private static final String WVDIAL = "wvdial";

  private static final Pattern LOCAL_IP = Pattern.compile("local\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern REMOTE_IP = Pattern.compile("remote\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern PRIMARY_DNS = Pattern.compile("primary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern SECONDARY_DNS = Pattern.compile("secondary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");

  private String local_ip = "";
  private String remote_ip = "";
  private String primary_dns = "";
  private String secondary_dns = "";

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
      LOG.info("LED " + LEDstate.STANDBY.name());
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
      LOG.info("LED " + LEDstate.RECORDING.name());
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
      LOG.info("LED " + LEDstate.ERROR.name());
    } catch (FileNotFoundException e) {
      LOG.error("cannot write to board LED file (no sudo)", e);
    }
  }

  @Override
  public void connectToMobileInternet() {
    try {
      Process process = Runtime.getRuntime().exec(WVDIAL);
      BufferedReader output = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      // wvdial always writes to error stream...
      String line = null;
      while ((line = output.readLine()) != null) {
        System.out.println(line);
        Matcher m1 = LOCAL_IP.matcher(line);
        Matcher m2 = REMOTE_IP.matcher(line);
        Matcher m3 = PRIMARY_DNS.matcher(line);
        Matcher m4 = SECONDARY_DNS.matcher(line);
        if (m1.find()) {
          this.local_ip = m1.group(1);
        } else if (m2.find()) {
          this.remote_ip = m2.group(1);
        } else if (m3.find()) {
          this.primary_dns = m3.group(1);
        } else if (m4.find()) {
          this.secondary_dns = m4.group(1);
          break;
        }
      }
      output.close();
      LOG.info("local ip is " + this.local_ip);
      LOG.info("remote ip is " + this.remote_ip);
      LOG.info("primary DNS is " + this.primary_dns);
      LOG.info("secondary DNS is " + this.secondary_dns);
    } catch (IOException e) {
      LOG.error("getting local controller address failed", e);
    }
  }

}
