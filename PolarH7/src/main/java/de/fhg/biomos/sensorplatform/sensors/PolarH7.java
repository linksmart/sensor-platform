package de.fhg.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensors.HeartRateSensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * @see {@link de.fhg.biomos.sensorplatform.gatt.PolarH7lib}
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends HeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  public PolarH7(SensorName name, String bdAddress, AddressType addressType, JSONObject sensorConfiguration) {
    super(name, bdAddress, addressType, sensorConfiguration);
  }

  /**
   * Enable heart rate notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag. Measurement contains the heart rate and optional one or more rr-intervals (if detected).
   *
   * @param charWriteCmd
   * @param enableNotification
   */
  private void enableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable heart rate and rr-interval notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   *
   * @param charWriteCmd
   * @param disableNotification
   */
  private void disableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + PolarH7lib.HANDLE_HEART_RATE_NOTIFICATION + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable heart rate and rr-interval notification");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enableHeartRateNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String diableNotification) {
    disableHeartRateNotification(streamToSensor, charWriteCmd, diableNotification);
  }

  /**
   * Calculate all values given.
   *
   * @param handle
   * @param rawHexValues
   * @return
   */
  public HeartRateSample calculateHeartRateSample(String timestamp, String handle, String rawHexValues) {
    if (handle.equals(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT)) {
      return calculateHeartRateData(timestamp, handle, rawHexValues);
    } else {
      LOG.error("unexpected handle address " + handle + " " + rawHexValues);
      return null;
    }
  }

}
