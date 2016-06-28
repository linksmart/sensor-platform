package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.bluetooth.HRM;
import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.main.Main;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.SensorType;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.sensors.SensorCommands}
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  private static final SimpleDateFormat formatter = new SimpleDateFormat(Main.timestampFormat);

  private PrintWriter hrmWriter = null;
  private PrintWriter rrWriter = null;
  private final File hrmFile;
  private final File rrFile;

  public PolarH7(SensorName name, String bdAddress, AddressType addressType, SensorType sensorType) {
    super(name, bdAddress, addressType, sensorType);
    this.hrmFile = new File(new File(new File(Main.sensorsDataDirectory, this.name.name()), "HRM"), "heartrate.log");
    this.rrFile = new File(new File(new File(Main.sensorsDataDirectory, this.name.name()), "RR"), "rrinterval.log");
    if (this.hrmFile.exists()) {
      this.hrmFile.delete();
    } else {
      this.hrmFile.getParentFile().mkdirs();
    }
    if (this.rrFile.exists()) {
      this.rrFile.delete();
    } else {
      this.rrFile.getParentFile().mkdirs();
    }
    try {
      this.hrmWriter = new PrintWriter(this.hrmFile, "UTF-8");
      this.hrmWriter.println("# " + name);
      LOG.info("using log file: " + this.hrmFile);
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    try {
      this.rrWriter = new PrintWriter(this.rrFile, "UTF-8");
      this.rrWriter.println("# " + name);
      LOG.info("using log file: " + this.rrFile);
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private void enableHeartRatelogging() throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("enable heart rate logging");
  }

  private void disableHeartRateLogging() throws IOException {
    this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("disable heart rate logging");
  }

  @Override
  public void enableLogging() {
    try {
      enableHeartRatelogging();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disableLogging() {
    try {
      disableHeartRateLogging();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.hrmWriter.close();
    this.rrWriter.close();
  }

  @Override
  public void processSensorData(String handle, String data) {
    if (handle.equals(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT)) {
      byte config = Byte.parseByte(data.substring(0, 2), 16);
      int heartrate = 0;
      Matcher matcher = null;

      if ((config & HRM.UINT16) == HRM.UINT16) {
        heartrate = Integer.parseInt(data.substring(3, 8).replace(" ", ""), 16);
        if ((config & HRM.RR_INTERVAL_AVAILABLE) == HRM.RR_INTERVAL_AVAILABLE) {
          matcher = HRM.PATTERN_RR_DATA.matcher(data.substring(9));
        }
      } else {
        heartrate = Integer.parseInt(data.substring(3, 5), 16);
        if ((config & HRM.RR_INTERVAL_AVAILABLE) == HRM.RR_INTERVAL_AVAILABLE) {
          matcher = HRM.PATTERN_RR_DATA.matcher(data.substring(6));
        }
      }
      String hrm = formatter.format(Calendar.getInstance().getTime()) + " " + heartrate + " Hz";
      System.out.println(hrm);
      this.hrmWriter.println(hrm);

      if ((config & HRM.SKIN_CONTACT_SUPPORTED) == HRM.SKIN_CONTACT_SUPPORTED) {
        if (!((config & HRM.SKIN_CONTACT_DETECTED) == HRM.SKIN_CONTACT_DETECTED)) {
          LOG.warn("no skin contact");
        }
      }

      if (matcher != null) {
        while (matcher.find()) {
          String tmp = matcher.group(0);
          tmp = tmp + tmp.substring(0, 2);
          tmp = tmp.substring(3);
          String rrinterval = formatter.format(Calendar.getInstance().getTime()) + " " + Integer.parseInt(tmp, 16) + " bpm/ms";
          System.out.println(rrinterval);
          this.rrWriter.println(rrinterval);
        }
      }
    } else {
      LOG.error("unexpected handle notification: " + handle);
    }
  }

}
