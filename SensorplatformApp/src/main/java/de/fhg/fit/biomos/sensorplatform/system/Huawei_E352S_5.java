package de.fhg.fit.biomos.sensorplatform.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for modem interaction. Use it as a singleton member in HardwarePlatform. TODO Refactor it to be a abstract to support different kind of modems.
 *
 * @author Daniel Pyka
 *
 */
public class Huawei_E352S_5 implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Huawei_E352S_5.class);

  private static final String RSSI_EXCELLENT = "excellent";
  private static final String RSSI_GOOD = "good";
  private static final String RSSI_FAIR = "fair";
  private static final String RSSI_POOR = "poor";
  private static final String RSSI_VERY_POOR = "very poor";
  private static final String RSSI_NO_SIGNAL = "no signal";

  // ^RSSI: 19
  private static final Pattern RSSI = Pattern.compile("\\^RSSI: (\\d+)");
  // ^DSFLOWRPT:00000210,00000000,00000000,0000000000000C58,0000000000000C5E,0010E000,0010E000
  private static final Pattern DSFLOWRPT = Pattern.compile("\\^DSFLOWRPT:(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+)");
  // ^CSNR: -145,-32
  private static final Pattern CSNR = Pattern.compile("\\^CSNR: (-\\d+),(-?+\\d+)");

  // huawei firmware specific AT command ^CSNR
  private static final String CSNR_QUERY = "AT^CSNR?\r";

  private static final File FILE = new File("/dev/ttyUSB2");

  private static final String COMGT_SERIAL = "comgt -d " + FILE + " sig";

  private int rssi = 0;
  private int rssi_dBm = -113;
  private int rscp = -145;
  private int ecio = -32;

  private int connectionDuration;
  private float measuredUploadSpeed_bps;
  private float measuredDownloadSpeed_bps;
  private long totalSentData;
  private long totalReceivedData;
  private int maxUploadSpeed_pbs;
  private int maxDownloadSpeed_bbs;

  private BufferedWriter streamToTTYUSB2 = null;
  private BufferedReader streamFromTTYUSB2 = null;

  private int dsflowprtCounter = 10;

  private boolean isRunning;

  public Huawei_E352S_5() {
    this.isRunning = false;
  }

  public void setupSerialPort() throws IOException, InterruptedException {
    LOG.info("setup serial port {}", FILE);
    Process p = Runtime.getRuntime().exec(COMGT_SERIAL);
    p.waitFor();
  }

  @Override
  public void run() {
    LOG.info("Logging {}", FILE);
    this.isRunning = true;
    try {
      this.streamToTTYUSB2 = new BufferedWriter(new FileWriter(FILE));
      this.streamFromTTYUSB2 = new BufferedReader(new FileReader(FILE));
      this.queryCSNR();
      String line = null;
      while ((line = this.streamFromTTYUSB2.readLine()) != null) {
        Matcher m1 = RSSI.matcher(line);
        Matcher m2 = DSFLOWRPT.matcher(line);
        Matcher m3 = CSNR.matcher(line);
        if (m1.find()) {
          this.rssi = Integer.parseInt(m1.group(1));
          this.rssi_dBm = rssiASUtoDBM(this.rssi);
          LOG.info("RSSI: {}, {}dBm, {}%, {}", this.rssi, rssiASUtoDBM(this.rssi), rssiASUtoDBMpercent(this.rssi), interpreteRSSI(this.rssi));
        } else if (m2.find()) {
          if (this.dsflowprtCounter % 10 == 0) {
            this.connectionDuration = Integer.parseInt(m2.group(1), 16);
            this.measuredUploadSpeed_bps = Integer.parseInt(m2.group(2), 16) / 1000;
            this.measuredDownloadSpeed_bps = Integer.parseInt(m2.group(3), 16) / 1000;
            this.totalSentData = Integer.parseInt(m2.group(4), 16);
            this.totalReceivedData = Integer.parseInt(m2.group(5), 16);
            this.maxUploadSpeed_pbs = Integer.parseInt(m2.group(6), 16) / 1000;
            this.maxDownloadSpeed_bbs = Integer.parseInt(m2.group(7), 16) / 1000;
            LOG.info("connection duration: {}s", this.connectionDuration);
            LOG.info("measured upload speed: {}kB/s", this.measuredUploadSpeed_bps);
            LOG.info("measured download speed: {}kB/s", this.measuredDownloadSpeed_bps);
            LOG.info("total sent bytes: {}", this.totalSentData);
            LOG.info("total received bytes: {}", this.totalReceivedData);
            LOG.info("maximum upload speed: {}kB/s", this.maxUploadSpeed_pbs);
            LOG.info("maximum download speed: {}kB/s", this.maxDownloadSpeed_bbs);
          }
          this.dsflowprtCounter++;
        } else if (m3.find()) {
          this.rscp = Integer.parseInt(m3.group(1));
          this.ecio = Integer.parseInt(m3.group(2));
          LOG.info("RSCP: {}dBm, Ec/Io: {}dB, RSSI: {}dBm", this.rscp, this.ecio, this.rscp - this.ecio);
        }
      }
      this.streamFromTTYUSB2.close();
      this.streamToTTYUSB2.close();
      LOG.info("{} streams closed", FILE);
    } catch (IOException e) {
      LOG.error("stream error related to {}", FILE);
    }
    this.isRunning = false;
    LOG.info("thread finished");
  }

  public int getRSSI() {
    return this.rssi_dBm;
  }

  public int getRSCP() {
    return this.rscp;
  }

  public int getECIO() {
    return this.ecio;
  }

  public void queryCSNR() {
    try {
      this.streamToTTYUSB2.write(CSNR_QUERY);
      this.streamToTTYUSB2.flush();
    } catch (IOException e) {
      LOG.error("cannot open file", e);
    } catch (NullPointerException e) {
      LOG.error("start thread before writing to {}", FILE);
    }
  }

  public boolean isAttached() {
    return FILE.exists();
  }

  public boolean isRunning() {
    return this.isRunning;
  }

  public int getConnectionDuration() {
    return this.connectionDuration;
  }

  public float getMeasuredUploadSpeed_bps() {
    return this.measuredUploadSpeed_bps;
  }

  public float getMeasuredDownloadSpeed_bps() {
    return this.measuredDownloadSpeed_bps;
  }

  public long getTotalSentData() {
    return this.totalSentData;
  }

  public long getTotalReceivedData() {
    return this.totalReceivedData;
  }

  public int getMaxUploadSpeed_pbs() {
    return this.maxUploadSpeed_pbs;
  }

  public int getMaxDownloadSpeed_bbs() {
    return this.maxDownloadSpeed_bbs;
  }

  /**
   *
   * @param asu
   *          first number of the output from modem command AT+CSQ
   * @return RSSI in dBm
   */
  public int rssiASUtoDBM(int asu) {
    return (-113) + asu * 2;
  }

  /**
   *
   * @param asu
   *          raw asu value
   * @return signal strength rssi percentage
   */
  public int rssiASUtoDBMpercent(int asu) {
    return Math.round(new Float(asu) / 31 * 100);
  }

  /**
   * Interprete the signal strength rssi.
   *
   * @param asu
   *          raw asu value
   * @return String marginal, OK, good, excellent, invalid range
   */
  public String interpreteRSSI(int asu) {
    if (asu >= 0 && asu < 2) {
      return RSSI_NO_SIGNAL;
    } else if (asu > 1 && asu < 8) {
      return RSSI_VERY_POOR;
    } else if (asu > 7 && asu < 14) {
      return RSSI_POOR;
    } else if (asu > 13 && asu < 20) {
      return RSSI_FAIR;
    } else if (asu > 19 && asu < 26) {
      return RSSI_GOOD;
    } else if (asu > 25 && asu <= 31) {
      return RSSI_EXCELLENT;
    } else {
      return "invalid range";
    }
  }

}
