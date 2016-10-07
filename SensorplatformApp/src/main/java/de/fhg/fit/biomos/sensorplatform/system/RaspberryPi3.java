package de.fhg.fit.biomos.sensorplatform.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;

/**
 * Functionality for the Sensorplatform running on a Raspberry Pi 3 with Raspbian (lite).
 *
 * @author Daniel Pyka
 *
 */
public class RaspberryPi3 implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(RaspberryPi3.class);

  private static final String LED0_TRIGGER = "/sys/class/leds/led0/trigger";
  private static final String LED0_DELAY_ON = "/sys/class/leds/led0/delay_on";
  private static final String LED0_DELAY_OFF = "/sys/class/leds/led0/delay_off";
  private static final String BLINK_INTERVAL = "1000";

  private static final String WVDIAL = "wvdial";
  private static final String COMGT = "comgt sig";

  private static final String QUALITY_MARGINAL = "marginal";
  private static final String QUALITY_OK = "OK";
  private static final String QUALITY_GOOD = "good";
  private static final String QUALITY_EXCELLENT = "excellent";

  private static final Pattern ATCSQ = Pattern.compile("\\+CSQ: (\\d+),(\\d+)");
  private static final Pattern PID_PPPD = Pattern.compile("Pid of pppd: (\\d+)");
  private static final Pattern LOCAL_IP = Pattern.compile("local\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern REMOTE_IP = Pattern.compile("remote\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern PRIMARY_DNS = Pattern.compile("primary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern SECONDARY_DNS = Pattern.compile("secondary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");

  private static final Pattern COMGT_SIGNALSTRENGTH = Pattern.compile("Signal Quality: (\\d+),(\\d+)");

  private static final String INTERNET_INTERFACE_NAME = "ppp0";

  private final Hciconfig hciconfig;
  private final Hcitool hcitool;

  private final Long internetCheckInterval;
  private boolean mobileInternet;
  private int signalQuality = 99;

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

  @Inject
  public RaspberryPi3(@Named("internet.check.interval") String internetCheckInterval) {
    this.internetCheckInterval = new Long(internetCheckInterval) * 1000;
    this.mobileInternet = false;
    this.hciconfig = new HciconfigImpl();
    this.hcitool = new HcitoolImpl();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        retrieveSignalQuality();
      } catch (IOException | InterruptedException e) {
        LOG.error("cannot retrieve signal quality", e);
      }
      try {
        this.mobileInternet = hasMobileInternetConnection();
      } catch (SocketException | NullPointerException e) {
        LOG.info("interface not running - starting daemon now");
        this.mobileInternet = false;
        connectToMobileInternet();
      }
      try {
        Thread.sleep(this.internetCheckInterval);
      } catch (InterruptedException e) {
        LOG.error("sleep failed", e);
      }
    }
    LOG.info("daemon thread finished");
  }

  @Override
  public boolean isConnectedToMobileInternet() {
    return this.mobileInternet;
  }

  @Override
  public int getMobileInternetSignalQuality() {
    return this.signalQuality;
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
  public Hciconfig getBluetoothController() {
    return this.hciconfig;
  }

  @Override
  public Hcitool getHcitool() {
    return this.hcitool;
  }

  /**
   * Retrieve the signal quality from the surf stick via a helper program comgt and store it in an internal variable.
   */
  private void retrieveSignalQuality() throws IOException, InterruptedException {
    Process process = Runtime.getRuntime().exec(COMGT);
    BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    while ((line = output.readLine()) != null) {
      Matcher m = COMGT_SIGNALSTRENGTH.matcher(line);
      if (m.find()) {
        this.signalQuality = CSQtoDBM(m.group(1));
        LOG.info("signal quality is " + this.signalQuality + " dBm, considered to be " + interpreteSignalQuality(m.group(1)));
      }
    }
    output.close();
    process.waitFor();
  }

  /**
   * Check if the network interface for mobile internet is up and running.
   *
   * @return true if ppp0 is up, false otherwise
   * @throws SocketException
   *           if there is an error accessing a socket
   * @throws NullPointerException
   *           if ppp0 does not exist
   */
  private boolean hasMobileInternetConnection() throws SocketException, NullPointerException {
    NetworkInterface inet = NetworkInterface.getByName(INTERNET_INTERFACE_NAME);
    LOG.info(inet.getDisplayName() + " " + inet.getInetAddresses().nextElement().getHostAddress() + " " + inet.isUp());
    return inet.isUp();
  }

  @Override
  public void connectToMobileInternet() {
    try {
      Process process = Runtime.getRuntime().exec(WVDIAL);
      BufferedReader output = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      // wvdial always writes to error stream...
      String line = null;
      while ((line = output.readLine()) != null) {
        // System.out.println(line);
        Matcher m1 = LOCAL_IP.matcher(line);
        Matcher m2 = REMOTE_IP.matcher(line);
        Matcher m3 = PRIMARY_DNS.matcher(line);
        Matcher m4 = SECONDARY_DNS.matcher(line);
        Matcher m5 = ATCSQ.matcher(line);
        Matcher m6 = PID_PPPD.matcher(line);
        if (m5.find()) {
          LOG.info("signal quality is " + m5.group(1) + " CSQ or " + CSQtoDBM(m5.group(1)) + " dBm");
          LOG.info("bit error rate (RxQual) is " + m5.group(2) + " (99: not supported)");
        } else if (m6.find()) {
          LOG.info("process id is " + m6.group(1));
        } else if (m1.find()) {
          LOG.info("local ip is " + m1.group(1));
        } else if (m2.find()) {
          LOG.info("remote ip is " + m2.group(1));
        } else if (m3.find()) {
          LOG.info("primary DNS is " + m3.group(1));
        } else if (m4.find()) {
          LOG.info("secondary DNS is " + m4.group(1));
          break;
        }
      }
      output.close();
      process.destroy(); // after upgrading the OS wvdial does not terminate anymore, force it
      process.waitFor();
      LOG.info("wvdial terminated");
    } catch (IOException | InterruptedException e) {
      LOG.error("getting local controller address failed", e);
    }
  }

  /**
   *
   * @param csq
   *          first number of the output from modem command AT+CSQ
   * @return unit in dBm
   */
  private int CSQtoDBM(String csq) {
    return (-113) + new Integer(csq) * 2;
  }

  /**
   * Interprete the signal quality value.
   *
   * @param csq
   *          raw csq value
   * @return String marginal, OK, good, excellent
   */
  private String interpreteSignalQuality(String csq) {
    int val = Integer.parseInt(csq);
    if (val >= 0 && val < 10) {
      return QUALITY_MARGINAL;
    } else if (val > 9 && val < 15) {
      return QUALITY_OK;
    } else if (val > 14 && val < 20) {
      return QUALITY_GOOD;
    } else if (val > 19 && val <= 30) {
      return QUALITY_EXCELLENT;
    } else {
      return "unknown range";
    }
  }

}
