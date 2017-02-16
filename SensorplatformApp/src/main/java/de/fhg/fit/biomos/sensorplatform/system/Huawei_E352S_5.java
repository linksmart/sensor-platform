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
import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

/**
 * Class for modem interaction. Use it as a singleton member in HardwarePlatform. TODO Refactoring would be required: Add abstract class to support different
 * kind of modems.
 *
 * @author Daniel Pyka
 *
 */
public class Huawei_E352S_5 implements Surfstick {

  private static final Logger LOG = LoggerFactory.getLogger(Huawei_E352S_5.class);

  // ^RSSI: 19
  private static final Pattern RSSI = Pattern.compile("\\^RSSI: (\\d+)");
  // ^DSFLOWRPT:00000210,00000000,00000000,0000000000000C58,0000000000000C5E,0010E000,0010E000
  private static final Pattern DSFLOWRPT = Pattern.compile("\\^DSFLOWRPT:(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+),(\\w+)");
  // ^CSNR: -145,-32
  private static final Pattern CSNR = Pattern.compile("\\^CSNR: (-\\d+),(-?+\\d+)");
  // ^SYSINFO:2,3,0,3,1,,3
  private static final Pattern SYSINFO = Pattern.compile("\\^SYSINFO:(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d*),(\\d*)");

  private static final File FILE = new File("/dev/ttyUSB2");   //sakis über 0

  private static final String COMGT_SERIAL = "comgt -d " + FILE + " sig";

  private static final String WVDIAL = "wvdial";

  private HardwarePlatform hwPlatform;

  private int overall_rssi = 0;
  private int overall_rssi_dBm = -113;

  private SignalQualityBean sqb = new SignalQualityBean(SignalQualityBean.RSCP_DEFAULT, SignalQualityBean.ECIO_DEFAULT);

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

  @Override
  public void setHardwarePlatform(HardwarePlatform hwPlatform) {
    this.hwPlatform = hwPlatform;
  }

  @Override
  public String getConnectCommand() {
    return WVDIAL;
  }

  @Override
  public void setupSerialPort() throws IOException, InterruptedException {
    Thread.sleep(30000);
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
          this.sqb = new SignalQualityBean(Integer.parseInt(m3.group(1)), Integer.parseInt(m3.group(2)));
          LOG.info("RSCP: {}dBm, Ec/Io: {}dB, RSSI: {}dBm, {}", this.sqb.getRSCP(), this.sqb.getECIO(), this.sqb.getRSSI(),
              GSM_GPRS_EDGE.evaluateRSSI(this.sqb.getRSSI()));
          this.hwPlatform.evaluateSignalQuality();
        } else if (m4.find()) {
          LOG.info("Modem (mode, submode): {}, {}", HuaweiUtils.getSystemModeName(Integer.parseInt(m4.group(4))),
              HuaweiUtils.getSystemSubmodeName(Integer.parseInt(m4.group(7))));
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

  @Override
  public int getOverallRSSI() {
    return this.overall_rssi_dBm;
  }

  @Override
  public SignalQualityBean getSQB() {
    return this.sqb;
  }

  @Override
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

  @Override
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

  @Override
  public boolean isAttached() {
    return FILE.exists();
  }

  @Override
  public boolean isRunning() {
    return this.isRunning;
  }

  @Override
  public int getConnectionDuration() {
    return this.connectionDuration;
  }

  @Override
  public float getMeasuredUploadSpeed_bps() {
    return this.measuredUploadSpeed_bps;
  }

  @Override
  public float getMeasuredDownloadSpeed_bps() {
    return this.measuredDownloadSpeed_bps;
  }

  @Override
  public long getTotalSentData() {
    return this.totalSentData;
  }

  @Override
  public long getTotalReceivedData() {
    return this.totalReceivedData;
  }

  @Override
  public int getMaxUploadSpeed_pbs() {
    return this.maxUploadSpeed_pbs;
  }

  @Override
  public int getMaxDownloadSpeed_bbs() {
    return this.maxDownloadSpeed_bbs;
  }

}
