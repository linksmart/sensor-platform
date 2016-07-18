package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.IOException;
import java.util.Properties;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.TomTomHRMlib;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.sensors.HeartRateSensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.Unit;
import de.fhg.fit.biomos.sensorplatform.web.DITGuploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.TomTomHRMlib}
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHRM extends HeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomHRM.class);

  private static final String HEARTRATE = "hrm";

  private SampleLogger sampleLogger = null;

  private Uploader uploader;

  public TomTomHRM(Properties properties, SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
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
   * SensorTag. This sensor only measures the heart rate, no rr-interval.
   */
  private void enableHeartRateNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + TomTomHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + enableNotification);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   */
  private void disableHeartRateNotification(String charWriteCmd, String disableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + TomTomHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification(String charWriteCmd, String enableNotification) {
    this.sampleLogger = new SampleLogger(HEARTRATE, this.name.name());
    this.sampleLogger.addDescriptionLine("Heartrate [" + Unit.BPM + "]");
    enableHeartRateNotification(charWriteCmd, enableNotification);
  }

  @Override
  public void disableNotification(String charWriteCmd, String disableNotification) {
    disableHeartRateNotification(charWriteCmd, disableNotification);
    this.sampleLogger.close();
  }

  @Override
  public void processSensorData(String handle, String rawHexValues) {
    if (handle.equals(TomTomHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      String timestamp = this.dtf.print(new DateTime());

      String heartrate = Integer.toString(getHeartRate8Bit(rawHexValues));

      if (this.fileLogging) {
        this.sampleLogger.write(timestamp, heartrate);
      }
      if (this.consoleLogging) {
        System.out.println(timestamp + " " + heartrate);
      }
      if (this.uploader != null) {
        this.uploader.sendData(this.bdAddress, "HeartRate", heartrate, "bpm");
      }
    } else {
      LOG.error("unexpected handle notification " + handle + " " + rawHexValues);
    }
  }

}
