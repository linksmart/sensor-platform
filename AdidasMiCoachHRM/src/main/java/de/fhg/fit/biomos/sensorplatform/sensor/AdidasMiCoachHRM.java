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

  public AdidasMiCoachHRM(SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
    super(name, bdAddress, addressType, sensorConfiguration);
  }

  /**
   * Enable heart rate notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag. This sensor only measures the heart rate, no rr-interval.
   */
  private void enableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + AdidasMiCoachHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   */
  private void disableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + AdidasMiCoachHRMlib.HANDLE_HEART_RATE_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable heart rate notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enableHeartRateNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    disableHeartRateNotification(streamToSensor, charWriteCmd, disableNotification);
  }

  public HeartRateSample calculateHeartRateSample(String timestamp, String handle, String rawHexValues) {
    if (handle.equals(AdidasMiCoachHRMlib.HANDLE_HEART_RATE_MEASUREMENT)) {
      return calculateHeartRateData(timestamp, handle, rawHexValues);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
      return null;
    }
  }

}
