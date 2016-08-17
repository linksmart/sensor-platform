package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.AdidasMiCoachHRMlib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensors.HeartRateSensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.gatt.TomTomHRMlib}
 *
 * @author Daniel Pyka
 *
 */
public class AdidasMiCoachHRM extends HeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(AdidasMiCoachHRMlib.class);

  public AdidasMiCoachHRM(SensorName name, String bdAddress, AddressType addressType, String timestampFormat, JSONObject sensorConfiguration) {
    super(name, bdAddress, addressType, timestampFormat, sensorConfiguration);
  }

  /**
   * Enable heart rate notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag. This sensor only measures the heart rate, no rr-interval.
   */
  private void enableHeartRateNotification(String charWriteCmd, String enableNotification) {
    try {
      this.bw.write(charWriteCmd + " " + AdidasMiCoachHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + enableNotification);
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
      this.bw.write(charWriteCmd + " " + AdidasMiCoachHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + disableNotification);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableNotification(BufferedWriter bw, String charWriteCmd, String enableNotification) {
    this.bw = bw;
    enableHeartRateNotification(charWriteCmd, enableNotification);
  }

  @Override
  public void disableNotification(String charWriteCmd, String disableNotification) {
    disableHeartRateNotification(charWriteCmd, disableNotification);
    this.bw = null;
  }

  public HeartRateSample calculateHeartRateSample(String handle, String rawHexValues) {
    if (handle.equals(AdidasMiCoachHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      return calculateHeartRateData(handle, rawHexValues);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
      return null;
    }
  }

}
