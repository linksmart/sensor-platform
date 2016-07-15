package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.InvalidSensorDataException;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.Unit;
import de.fhg.fit.biomos.sensorplatform.web.DITGuploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib}
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends HeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  private static final String HEARTRATE = "hrm";
  private static final String RRINTERVAL = "rr";

  Map<String, SampleLogger> sampleLoggers = new HashMap<String, SampleLogger>();

  private Uploader uploader;

  public PolarH7(Properties properties, SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
    super(properties, name, bdAddress, addressType, sensorConfiguration);

    // Modifiy in case of different webinterfaces
    switch (this.webinterface) {
      case "ditg":
        this.uploader = new DITGuploader(properties);
        this.uploader.login();
        break;
      case "":
        LOG.info("No webinterface specified");
        break;
      default:
        LOG.error("Unknown webinterface name: " + this.webinterface);
        break;
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
    if (this.fileLogging) {
      this.sampleLoggers.put(HEARTRATE, new SampleLogger(HEARTRATE, this.name.name()));
      this.sampleLoggers.get(HEARTRATE).addDescriptionLine("Heartrate [" + Unit.BPM + "]");
      this.sampleLoggers.put(RRINTERVAL, new SampleLogger(RRINTERVAL, this.name.name()));
      this.sampleLoggers.get(RRINTERVAL).addDescriptionLine("RR-Interval [" + Unit.BPMMS + "]");
    }
    enableHeartRateNotification();
  }

  @Override
  public void disableNotification() {
    disableHeartRateNotification();
    if (this.fileLogging) {
      this.sampleLoggers.get(HEARTRATE).close();
      this.sampleLoggers.get(RRINTERVAL).close();
    }
  }

  @Override
  public void processSensorData(String handle, String rawHexValues) {
    if (handle.equals(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT)) {
      String timestamp = this.dtf.print(new DateTime());
      String heartRate = Integer.toString(getHeartRate8Bit(rawHexValues));

      if (this.fileLogging) {
        this.sampleLoggers.get(HEARTRATE).write(timestamp, heartRate);
      }
      if (this.consoleLogging) {
        System.out.println(timestamp + " " + heartRate);
      }
      if (this.uploader != null) {
        this.uploader.sendData(this.bdAddress, "HeartRate", heartRate, "bpm");
      }

      try {
        if (!isSkinContactDetected(rawHexValues)) {
          LOG.warn("no skin contact!");
        }
      } catch (InvalidSensorDataException e) {
        LOG.error(e.getMessage());
      }

      if (isRRintervalDataAvailable(rawHexValues)) {
        try {
          List<Integer> rrintervals = getRRintervalsWith8BitHeartRateData(rawHexValues);
          StringBuilder sb = new StringBuilder();
          for (Integer rrinterval : rrintervals) {
            sb.append(rrinterval.toString() + " ");
          }
          String rrvalues = sb.toString().trim(); // remove space suffix
          if (this.fileLogging) {
            this.sampleLoggers.get(RRINTERVAL).write(timestamp, rrvalues);
          }
          if (this.consoleLogging) {
            System.out.println(timestamp + " " + rrvalues);
          }
        } catch (InvalidSensorDataException e) {
          LOG.error(e.getMessage());
        }
        // TODO upload rr intervals somewhere too
      }
    } else {
      LOG.error("unexpected handle notification " + handle + " : " + rawHexValues);
    }
  }

}
