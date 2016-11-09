package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.gatt.BLE113lib;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Abstraction for the physical sensor. Defines HOW specific functions of the sensor are activated/deactivated.
 *
 * @author Daniel Pyka
 *
 */
public class BLE113 extends AbstractPulseOximeterSensor {

  private static final Logger LOG = LoggerFactory.getLogger(BLE113.class);

  public BLE113(SensorName name, String bdAddress, JSONObject settings) {
    super(new BLE113lib(), name, bdAddress, AddressType.PUBLIC, SecurityLevel.HIGH, settings);
  }

  /**
   * Enable pulse oximeter notification of the sensor. Notification period is fixed at 1/s . The measurement does not need to be activated explicitly as in the
   * SensorTag, only the notification. This sensor measures normal SpO2 and pulse rate values.
   *
   * @param streamToSensor
   *          the stream to the gatttool
   * @param charWriteCmd
   *          the gatttool command for writing to a handle
   * @param enableNotification
   *          the bitmask for enabling notifications
   */
  private void enablePulseOximeterNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandlePulseOximeterNotification() + " " + enableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("enable pulse oximeter notification");
    } catch (IOException e) {
      LOG.error("cannot enable pulse oximeter notification", e);
    }
  }

  /**
   * Disable heart rate notification of the sensor.
   *
   * @param streamToSensor
   *          the stream to the gatttool
   * @param charWriteCmd
   *          the gatttool command for writing to a handle
   * @param disableNotification
   *          the bitmask for disabling notifications
   */
  private void disablePulseOximeterNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    try {
      streamToSensor.write(charWriteCmd + " " + this.gattLibrary.getHandlePulseOximeterNotification() + " " + disableNotification);
      streamToSensor.newLine();
      streamToSensor.flush();
      LOG.info("disable pulse oximeter notification");
    } catch (IOException e) {
      LOG.error("cannot disable pulse oximeter notification", e);
    }
  }

  @Override
  public void enableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enablePulseOximeterNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    disablePulseOximeterNotification(streamToSensor, charWriteCmd, disableNotification);
  }

}
