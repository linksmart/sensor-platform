package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.BLE113lib;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * The BLE113 with given firmware simulates a very simple Pulse Oximeter Sensor. Only the mandatory fields of this characteristic are implemented (Control Byte,
 * SpO2PR-Normal - SpO2 and SpO2PR-Normal - PR). Furthermore, the checks for the control byte are not implemented. This test covers only the calculation of the
 * SpO2PR Normal fields. The characteristic features a lot more information, which would require a "real" sensor, not a development board with custom firmware.
 * For detecting which data would be available, all checks for the control byte are also mandatory.
 *
 * @see <a href= "https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.plx_continuous_measurement.xml"> PLX
 *      Continuous Measurement Characteristic</a>
 *
 * @author Daniel Pyka
 *
 */
public class BLE113Test {

  private static final String pulseOximeterData1 = "00 00 62 00 3C";
  private static final String pulseOximeterData2 = "00 00 5A 00 78";
  private static final String pulseOximeterData3 = "00 00 4F 00 B4";

  private static BLE113 ble113;

  private static PulseOximeterSample pos1;
  private static PulseOximeterSample pos2;
  private static PulseOximeterSample pos3;

  @BeforeClass
  public static void setup() {
    ble113 = new BLE113(SensorName.BLE113, BLE113lib.DEFAULT_BDADDRESS, new JSONObject());
    pos1 = ble113.calculatePulseOximeterData("2016-09-02T08:45:30.555Z", ble113.gattLibrary.getHandlePulseOximeterMeasurement(), pulseOximeterData1);
    pos2 = ble113.calculatePulseOximeterData("2016-09-01T23:12:55.378Z", ble113.gattLibrary.getHandlePulseOximeterMeasurement(), pulseOximeterData2);
    pos3 = ble113.calculatePulseOximeterData("2016-08-31T15:47:08.160Z", ble113.gattLibrary.getHandlePulseOximeterMeasurement(), pulseOximeterData3);
  }

  @Test
  public void testControlByte() {
    // not implemented
  }

  @Test
  public void testSpO2NormalSpO2() {
    Assert.assertEquals(Integer.valueOf(98), BLE113Test.pos1.getSpO2());
    Assert.assertEquals(Integer.valueOf(90), BLE113Test.pos2.getSpO2());
    Assert.assertEquals(Integer.valueOf(79), BLE113Test.pos3.getSpO2());
  }

  @Test
  public void testSpO2NormalPulseRate() {
    Assert.assertEquals(Integer.valueOf(60), BLE113Test.pos1.getPulseRate());
    Assert.assertEquals(Integer.valueOf(120), BLE113Test.pos2.getPulseRate());
    Assert.assertEquals(Integer.valueOf(180), BLE113Test.pos3.getPulseRate());
  }

}
