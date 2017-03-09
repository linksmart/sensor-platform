package de.fhg.fit.biomos.sensorplatform.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

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

  private static final String INTERFACE_MOBILE_INTERNET = "ppp0";
  // private static final String INTERFACE_CABLE = "eth0";
  // private static final String INTERFACE_WIFI = "wlan0";

  private static final Pattern LOCAL_IP = Pattern.compile("local\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern REMOTE_IP = Pattern.compile("remote\\s+IP\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern PRIMARY_DNS = Pattern.compile("primary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");
  private static final Pattern SECONDARY_DNS = Pattern.compile("secondary\\s+DNS\\s+address\\s+(\\d+.\\d+.\\d+\\d+.\\d+)");

  private final Hcitool hcitool;

  private final Surfstick surfstick;
  private Thread surfstickThread;

  private final Long internetCheckInterval;
  private boolean mobileInternet;
  private boolean uploadPermitted;

  private final int criticalValueRSSI;
  private final int criticalValueRSCP;
  private final int criticalValueECIO;

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
  public RaspberryPi3(Surfstick surfstick, @Named("surfstick.check.interval") String internetCheckInterval,
      @Named("critical.value.rssi") String criticalValueRSSI, @Named("critical.value.rscp") String criticalValueRSCP,
      @Named("critical.value.ecio") String criticalValueECIO) {
    this.internetCheckInterval = new Long(internetCheckInterval) * 1000;
    this.mobileInternet = false;
    this.uploadPermitted = false;
    this.criticalValueRSSI = Integer.parseInt(criticalValueRSSI);
    this.criticalValueRSCP = Integer.parseInt(criticalValueRSCP);
    this.criticalValueECIO = Integer.parseInt(criticalValueECIO);
    this.hcitool = new HcitoolImpl();
    this.surfstick = surfstick;
    this.surfstick.setHardwarePlatform(this);
  }

  private void setupSerialPort() {
    try {
      this.surfstick.setupSerialPort();
    } catch (IOException ioe) {
      LOG.info("comgt crashed - modem serial port may not work correctly", ioe);
    } catch (InterruptedException ie) {
      LOG.info("wait for comgt failed", ie);
    }
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      // if (this.surfstick.isAttached()) {
      if (hasMobileInternetConnection()) {
        this.mobileInternet = true;
        /*
         * if (!this.surfstick.isRunning()) { //weg this.surfstickThread = new Thread(this.surfstick, "serialport"); this.surfstickThread.start(); } else {
         * this.surfstick.querySYSINFO(); }
         */
      } else {
        // this.uploadPermitted = false;
        this.mobileInternet = false;
        // setupSerialPort(); //braucht man nicht
        connectToMobileInternet();
        continue;
      }
      /*
       * } else { this.uploadPermitted = false; this.mobileInternet = false; LOG.warn("surfstick not attached"); }
       */

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
  public int getOverallRSSIfromMobileInternet() {
    return this.surfstick.getOverallRSSI();
  }

  @Override
  public SignalQualityBean getSignalQualityBean() {
    return this.surfstick.getSQB();
  }

  @Override
  public void evaluateSignalQuality() {

    if (this.surfstick.getSQB().getECIO() < this.criticalValueECIO || this.surfstick.getSQB().getRSCP() < this.criticalValueRSCP
        || this.surfstick.getSQB().getRSSI() < this.criticalValueRSSI) {
      if (this.uploadPermitted) {
        LOG.info("----------");
        LOG.info("suspend upload");
        LOG.info("Reason: RSCP: {}dBm, Ec/Io: {}dB, RSSI: {}dBm", this.surfstick.getSQB().getRSCP(), this.surfstick.getSQB().getECIO(),
            this.surfstick.getSQB().getRSSI());
        LOG.info("----------");
        this.uploadPermitted = false;
      }
    } else {
      if (!this.uploadPermitted) {
        LOG.info("----------");
        LOG.info("permit upload");
        LOG.info("Reason: RSCP: {}dBm, Ec/Io: {}dB, RSSI: {}dBm", this.surfstick.getSQB().getRSCP(), this.surfstick.getSQB().getECIO(),
            this.surfstick.getSQB().getRSSI());
        LOG.info("----------");
        this.uploadPermitted = true;
      }
    }

  }

  @Override
  public boolean isUploadPermitted() {
    return this.uploadPermitted;
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
      LOG.info("LED {}", LEDstate.STANDBY.name());
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
      LOG.info("LED {}", LEDstate.RECORDING.name());
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
      LOG.info("LED {}", LEDstate.ERROR.name());
    } catch (FileNotFoundException e) {
      LOG.error("cannot write to board LED file (no sudo)", e);
    }
  }

  @Override
  public Hcitool getHcitool() {
    return this.hcitool;
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
  private boolean hasMobileInternetConnection() {
    try {
      NetworkInterface inet = NetworkInterface.getByName(INTERFACE_MOBILE_INTERNET);
      LOG.info("{} {} {}", inet.getDisplayName(), inet.getInetAddresses().nextElement().getHostAddress(), inet.isUp());
      return true;
    } catch (IOException | NullPointerException e) {
      return false;
    }
  }

  // private boolean hasCableNetworkInternetConnection() {
  // try {
  // NetworkInterface inet = NetworkInterface.getByName(INTERFACE_CABLE);
  // return inet.isUp();
  // } catch (IOException | NullPointerException e) {
  // return false;
  // }
  // }

  @Override
  public void connectToMobileInternet() {
    //LOG.info("Trying to connect to mobile internet using sakis3g");
    try {
      // Process process = Runtime.getRuntime().exec(this.surfstick.getConnectCommand());
      // List<String> command = new ArrayList<>();
      // command.add("/home/administrator/3g/sakis3g");
      // command.add("SIM_PIN=1983");
      // command.add("--sudo");
      // command.add("connect");
      ProcessBuilder pb = this.surfstick.getMobileInternetPB();
      // pb.directory(new File("/home/administrator/3g"));
      // pb.command("/home/administrator/3g/sakis3g", "SIM_PIN=1983", "--sudo", "connect");
      // Process process = Runtime.getRuntime().exec("/home/administrator/3g/sakis3g SIM_PIN=1983 --sudo connect");
      Process process = pb.start();

      // BufferedReader output = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

      // wvdial always writes to error stream...
      String line = null;
      while ((line = in.readLine()) != null) {
        // System.out.println(line); // extreme debugging
        // process = Runtime.getRuntime().exec(this.surfstick.getConnectCommand());
       // LOG.info(line);

        /*
         * Matcher m1 = LOCAL_IP.matcher(line); Matcher m2 = REMOTE_IP.matcher(line); Matcher m3 = PRIMARY_DNS.matcher(line); Matcher m4 =
         * SECONDARY_DNS.matcher(line); if (m1.find()) { LOG.info("local ip is {}", m1.group(1)); } else if (m2.find()) { LOG.info("remote ip is {}",
         * m2.group(1)); } else if (m3.find()) { LOG.info("primary DNS is {}", m3.group(1)); } else if (m4.find()) { LOG.info("secondary DNS is {}",
         * m4.group(1)); break; }
         */
      }
      // LOG.info(line);
      in.close();
      // process.destroy();
      process.waitFor();
      //LOG.info("sakis3g exit value (0=OK): " + process.exitValue());
      // LOG.info("sakis3g process terminated");
    } catch (IOException | InterruptedException e) {
      LOG.error("cannot connect to mobile internet", e);
    }
  }

}
