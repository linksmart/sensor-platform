package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.IOException;
import java.util.Properties;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.TomTomAndAdidasHRMlib;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.util.Unit;
import de.fhg.fit.biomos.sensorplatform.web.DITGuploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.TomTomAndAdidasHRMlib}
 *
 * @author Daniel Pyka
 *
 */
public class TomTomAdidas extends Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomAdidas.class);

  private static final String HEARTRATE = "hrm";

  private SampleLogger sampleLogger = null;

  private Uploader uploader;

  public TomTomAdidas(Properties properties, SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
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
  private void enableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + TomTomAndAdidasHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.ENABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("enable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void disableHeartRateNotification() {
    try {
      this.bw.write(GatttoolImpl.CMD_CHAR_WRITE_CMD + " " + TomTomAndAdidasHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + GatttoolImpl.DISABLE_NOTIFICATION);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification() {
    this.sampleLogger = new SampleLogger(HEARTRATE, this.name.name());
    this.sampleLogger.addDescriptionLine("Heartrate [" + Unit.BPM + "]");
    enableHeartRateNotification();
  }

  @Override
  public void disableNotification() {
    disableHeartRateNotification();
    this.sampleLogger.close();
  }

  @Override
  public void processSensorData(String handle, String rawHexValues) {
    if (handle.equals(TomTomAndAdidasHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      String timestamp = this.dtf.print(new DateTime());
      // byte config = Byte.parseByte(rawHexValues.substring(0, 2), 16);
      // int heartrate = 0;

      // we know, that...
      // 1. hrm value is always 8bit from from TomTom/Adidas
      // 2. there is no rr data available
      // so we do not need to check the whole configuration byte
      // if ((config & HRM.UINT16) == HRM.UINT16) {
      // heartrate = Integer.parseInt(rawHexValues.substring(3, 8).replace(" ", ""), 16);
      // } else {
      // heartrate = Integer.parseInt(rawHexValues.substring(3, 5), 16);
      // }

      String heartrate = Integer.toString(Integer.parseInt(rawHexValues.substring(3, 5), 16));

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
