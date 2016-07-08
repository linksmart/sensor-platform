package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.bluetooth.HRM;
import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.sensors.SensorCommands}
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  // private final HttpUploader uploader;

  public PolarH7(Properties properties, SensorName name, String bdAddress, AddressType addressType) {
    super(properties, name, bdAddress, addressType);

    // TODO what about mulitple web interfaces -> reflection?
    // this.uploader = new DITGhttpUploader(properties);
    // this.uploader.login();
  }

  private void enableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification() {
    this.sampleLoggers.put("hrm", new SampleLogger(this.properties, "hrm", this.name.name()));
    this.sampleLoggers.put("rr", new SampleLogger(this.properties, "rr", this.name.name()));
    enableHeartRateNotification();
  }

  @Override
  public void disableNotification() {
    disableHeartRateNotification();
    this.sampleLoggers.get("hrm").close();
    this.sampleLoggers.get("rr").close();
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
      String hrm = heartrate + " Hz";
      this.sampleLoggers.get("hrm").write(hrm);
      // this.uploader.sendData(this.bdAddress, "HeartRate", hrm, "bpm");

      if ((config & HRM.SKIN_CONTACT_SUPPORTED) == HRM.SKIN_CONTACT_SUPPORTED) {
        if (!((config & HRM.SKIN_CONTACT_DETECTED) == HRM.SKIN_CONTACT_DETECTED)) {
          LOG.warn("no skin contact!");
        }
      }

      if (matcher != null) {
        while (matcher.find()) {
          String tmp = matcher.group(0);
          tmp = tmp + tmp.substring(0, 2);
          tmp = tmp.substring(3);
          String rrinterval = Integer.parseInt(tmp, 16) + " bpm/ms";
          this.sampleLoggers.get("rr").write(rrinterval);
        }
      }
    } else {
      LOG.error("unexpected handle notification " + handle + " : " + data);
    }
  }

}
