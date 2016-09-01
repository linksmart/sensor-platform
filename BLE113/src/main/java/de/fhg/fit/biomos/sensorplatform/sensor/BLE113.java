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
 *
 * @author Daniel Pyka
 *
 */
public class BLE113 extends AbstractPulseOximeterSensor {

  private static final Logger LOG = LoggerFactory.getLogger(BLE113.class);

  private static final AddressType addressType = AddressType.PUBLIC;
  private static final SecurityLevel securityLevel = SecurityLevel.MEDIUM;

  public BLE113(SensorName name, String bdAddress, JSONObject settings) {
    super(new BLE113lib(), name, bdAddress, addressType, securityLevel, settings);
  }

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
  public void enableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {
    enablePulseOximeterNotification(streamToSensor, charWriteCmd, enableNotification);
  }

  @Override
  public void disableAllNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {
    disablePulseOximeterNotification(streamToSensor, charWriteCmd, disableNotification);
  }

}