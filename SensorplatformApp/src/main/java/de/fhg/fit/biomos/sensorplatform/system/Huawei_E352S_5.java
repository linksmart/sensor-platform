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

import de.fhg.fit.biomos.sensorplatform.util.GSM_GPRS_EDGE;
import de.fhg.fit.biomos.sensorplatform.util.HuaweiUtils;

/**
 * Class for modem interaction. Use it as a singleton member in HardwarePlatform. TODO Refactor it to be a abstract to support different kind of modems.
 *
 * @author Daniel Pyka
 *
 */
public class Huawei_E352S_5 implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Huawei_E352S_5.class);

  // ^RSSI: 19
  private static final Pattern RSSI = Pattern.compile("\\^RSSI: (\\d+)");
  // ^DSFLOWRPT:00000210,00000000,00000000,0000000000000C58,0000000000000C5E,0010E000,0010E000
  private static final Pattern DSFLOWRPT = Pattern.compile("\\^DSFLOWRPT:(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+)");
  // ^CSNR: -145,-32
  private static final Pattern CSNR = Pattern.compile("\\^CSNR: (-\\d+),(-?+\\d+)");
  // ^SYSINFO:2,3,0,3,1,,3
  private static final Pattern SYSINFO = Pattern.compile("\\^SYSINFO:(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d*),(\\d*)");

  private static final File FILE = new File("/dev/ttyUSB2");

  private static final String COMGT_SERIAL = "comgt -d " + FILE + " sig";

  private int overall_rssi = 0;
  private int overall_rssi_dBm = -113;
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

  private int dsflowprtCounter = 30;

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
      String line = null;
      while ((line = this.streamFromTTYUSB2.readLine()) != null) {
        Matcher m1 = RSSI.matcher(line);
        Matcher m2 = DSFLOWRPT.matcher(line);
        Matcher m3 = CSNR.matcher(line);
        Matcher m4 = SYSINFO.matcher(line);
        if (m1.find()) {
          this.overall_rssi = Integer.parseInt(m1.group(1));
          this.overall_rssi_dBm = GSM_GPRS_EDGE.rssiASUtoDBM(this.overall_rssi);
          LOG.info("Overall RSSI: {}, {}dBm, {}%", this.overall_rssi, GSM_GPRS_EDGE.rssiASUtoDBM(this.overall_rssi),
              GSM_GPRS_EDGE.rssiASUtoDBMpercent(this.overall_rssi));
        } else if (m2.find()) {
          if (this.dsflowprtCounter % 30 == 0) {
            this.connectionDuration = Integer.parseInt(m2.group(1), 16);
            this.measuredUploadSpeed_bps = Integer.parseInt(m2.group(2), 16) / 1000;
            this.measuredDownloadSpeed_bps = Integer.parseInt(m2.group(3), 16) / 1000;
            this.totalSentData = Integer.parseInt(m2.group(4), 16);
            this.totalReceivedData = Integer.parseInt(m2.group(5), 16);
            this.maxUploadSpeed_pbs = Integer.parseInt(m2.group(6), 16) / 1000;
            this.maxDownloadSpeed_bbs = Integer.parseInt(m2.group(7), 16) / 1000;
            LOG.info("----- surf stick status -----");
            LOG.info("connection duration: {}s", this.connectionDuration);
            LOG.info("measured upload speed: {}kB/s", this.measuredUploadSpeed_bps);
            LOG.info("measured download speed: {}kB/s", this.measuredDownloadSpeed_bps);
            LOG.info("total sent bytes: {}", this.totalSentData);
            LOG.info("total received bytes: {}", this.totalReceivedData);
            LOG.info("maximum upload speed: {}kB/s", this.maxUploadSpeed_pbs);
            LOG.info("maximum download speed: {}kB/s", this.maxDownloadSpeed_bbs);
            LOG.info("-----------------------------");
          }
          this.dsflowprtCounter++;
        } else if (m3.find()) {
          this.rscp = Integer.parseInt(m3.group(1));
          this.ecio = Integer.parseInt(m3.group(2));
          LOG.info("RSCP: {}dBm, Ec/Io: {}dB, RSSI: {}dBm, {}", this.rscp, this.ecio, this.rscp - this.ecio, GSM_GPRS_EDGE.evaluateRSSI(this.rscp - this.ecio));
        } else if (m4.find()) {
          LOG.info("Modem (mode, submode): {}, {}", HuaweiUtils.getSystemModeName(m4.group(4)), HuaweiUtils.getSystemSubmodeName(m4.group(7)));
          this.queryCSNR();
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

  public int getOverallRSSI() {
    return this.overall_rssi_dBm;
  }

  public int getRSCP() {
    return this.rscp;
  }

  public int getECIO() {
    return this.ecio;
  }

  public void queryCSNR() {
    try {
      this.streamToTTYUSB2.write(HuaweiUtils.CSNR_QUERY);
      this.streamToTTYUSB2.flush();
    } catch (IOException e) {
      LOG.error("cannot write to serial port", e);
    } catch (NullPointerException e) {
      LOG.error("start thread before writing to {}", FILE);
    }
  }

  public void querySYSINFO() {
    try {
      this.streamToTTYUSB2.write(HuaweiUtils.SYSINFO_QUERY);
      this.streamToTTYUSB2.flush();
    } catch (IOException e) {
      LOG.error("cannot write to serial port", e);
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

}
