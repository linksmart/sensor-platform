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
import de.fhg.fit.biomos.sensorplatform.util.SensorConfiguration;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.web.DITGhttpUploader;
import de.fhg.fit.biomos.sensorplatform.web.HttpUploader;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib}
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  private final boolean onlineMode;
  private HttpUploader uploader;

  private final SensorConfiguration sensorConfiguration;

  public PolarH7(Properties properties, SensorName name, String bdAddress, AddressType addressType, SensorConfiguration sensorConfiguration) {
    super(properties, name, bdAddress, addressType);

    this.sensorConfiguration = sensorConfiguration;

    this.onlineMode = new Boolean(this.sensorConfiguration.getSetting(SensorConfiguration.ONLINEMODE));
    LOG.info("online mode: " + this.onlineMode);

    // TODO make more dynamic?
    if (this.onlineMode && sensorConfiguration.getSetting(SensorConfiguration.WEBINTERFACE).equals("ditg")) {
      this.uploader = new DITGhttpUploader(properties);
      this.uploader.login();
    }
  }

  /**
   * Enable heart rate notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag. Measurement contains the heart rate and optional one or more rr-intervals (if detected).
   */
  private void enableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable heart rate and rr-interval notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   */
  private void disableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable heart rate and rr-interval notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification() {
    this.sampleLoggers.put(SensorConfiguration.HEARTRATE, new SampleLogger(this.properties, SensorConfiguration.HEARTRATE, this.name.name()));
    this.sampleLoggers.put(SensorConfiguration.RRINTERVAL, new SampleLogger(this.properties, SensorConfiguration.RRINTERVAL, this.name.name()));
    enableHeartRateNotification();
  }

  @Override
  public void disableNotification() {
    disableHeartRateNotification();
    this.sampleLoggers.get(SensorConfiguration.HEARTRATE).close();
    this.sampleLoggers.get(SensorConfiguration.RRINTERVAL).close();
  }

  @Override
  public void processSensorData(String handle, String rawHexValues) {
    if (handle.equals(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT)) {
      byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
      int heartrate = 0;
      Matcher rrData = null;

      // we know, that hrm value is always 8bit from PolarH7
      // so we do not need to check the whole configuration byte
      // if ((config & HRM.UINT16) == HRM.UINT16) {
      // heartrate = Integer.parseInt(rawHexValues.substring(3, 8).replace(" ", ""), 16);
      // if ((config & HRM.RR_INTERVAL_AVAILABLE) == HRM.RR_INTERVAL_AVAILABLE) {
      // matcher = HRM.PATTERN_RR_DATA.matcher(rawHexValues.substring(9));
      // }
      // } else {
      // heartrate = Integer.parseInt(rawHexValues.substring(3, 5), 16);
      // if ((config & HRM.RR_INTERVAL_AVAILABLE) == HRM.RR_INTERVAL_AVAILABLE) {
      // matcher = HRM.PATTERN_RR_DATA.matcher(rawHexValues.substring(6));
      // }
      // }

      heartrate = Integer.parseInt(rawHexValues.substring(3, 5), 16);
      if ((config & HRM.RR_INTERVAL_AVAILABLE) == HRM.RR_INTERVAL_AVAILABLE) {
        rrData = HRM.PATTERN_RR_DATA.matcher(rawHexValues.substring(6));
      }

      String hrm = heartrate + " Hz";
      this.sampleLoggers.get(SensorConfiguration.HEARTRATE).write(hrm);

      if (this.onlineMode) {
        this.uploader.sendData(this.bdAddress, "HeartRate", hrm, "bpm");
      }

      if ((config & HRM.SKIN_CONTACT_SUPPORTED) == HRM.SKIN_CONTACT_SUPPORTED) {
        if (!((config & HRM.SKIN_CONTACT_DETECTED) == HRM.SKIN_CONTACT_DETECTED)) {
          LOG.warn("no skin contact!");
        }
      }

      if (rrData != null) {
        while (rrData.find()) {
          String tmp = rrData.group(0);
          tmp = tmp + tmp.substring(0, 2);
          tmp = tmp.substring(3);
          String rrinterval = Integer.parseInt(tmp, 16) + " bpm/ms";
          this.sampleLoggers.get(SensorConfiguration.RRINTERVAL).write(rrinterval);
          // TODO upload rr intervals somewhere too
        }
      }
    } else {
      LOG.error("unexpected handle notification " + handle + " : " + rawHexValues);
    }
  }

}
