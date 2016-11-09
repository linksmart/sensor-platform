package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for the physical sensor. Defines HOW specific functions of the sensor are activated/deactivated.
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7 extends AbstractHeartRateSensor {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7.class);

  public PolarH7(SensorName name, String bdAddress, JSONObject sensorConfiguration) {
    super(new PolarH7lib(), name, bdAddress, AddressType.PUBLIC, SecurityLevel.LOW, sensorConfiguration);
  }

  /**
   * Enable heart rate notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag. This sensor only measures the heart rate, no rr-interval.
   *
   * @param streamToSensor
   *          gatttool input stream
   * @param charWriteCmd
   *          gatttool write command
   * @param enableNotification
   *          as defined in the bluetooth specification (01:00)
   */
  private void enableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandleHeartRateNotification() + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable heart rate and rr-interval notification");
    } catch (IOException e) {
      LOG.error("cannot enable heart rate notification", e);
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   *
   * @param streamToSensor
   *          gatttool input stream
   * @param charWriteCmd
   *          gatttool write command
   * @param enableNotification
   *          as defined in the bluetooth specification (01:00)
   */
  private void disableHeartRateNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandleHeartRateNotification() + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable heart rate and rr-interval notification");
    } catch (IOException e) {
      LOG.error("cannot disable heart rate notification", e);
    }
  }

  @Override
  public void enableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enableHeartRateNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String diableNotification) {
    disableHeartRateNotification(streamToSensor, charWriteCmd, diableNotification);
  }

}
