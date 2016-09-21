package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * This test ensures correct calculation of heart rate measurement data of the class AbstractHeartRateSensor. Only one concrete implementation is tested
 * (PolarH7). This is sufficient, because all heart rate sensors inherit from AbstractHeartRateSensor.
 *
 * @see <a href=
 *      "https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml">
 *      Bluetooth Heart Rate Measurement</a>
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7Test {

  // control byte CC: 00 to 1F
  // heart rate value HH/ HH HH: uint8/ uint16
  // energy expended EE: uint16
  // rr intervals (RR RR)*: uint16
  // for 16 bit values gatttool display order is
  // most significant octet (MSO) least significant octet (LSO)
  // a number "ABCD" (hex) is displayed as "CD AB" (hex)

  // CC HH EE EE RR RR RR RR
  private static final String heartrateData1 = "1E 44 64 00 9b 03 74 03";
  // CC HH EE EE
  private static final String heartrateData2 = "0E 44 64 00 9b";
  // CC HH RR RR RR RR
  private static final String heartrateData3 = "16 44 9b 03 74 03";
  // CC HH
  private static final String heartrateData4 = "06 44";
  // CC HH HH EE EE RR RR RR RR
  private static final String heartrateData5 = "1F 44 00 64 00 9b 03 74 03";
  // CC HH HH EE EE
  private static final String heartrateData6 = "0F 44 00 64 00";
  // CC HH HH RR RR RR RR
  private static final String heartrateData7 = "17 44 00 9b 03 74 03";
  // CC HH HH
  private static final String heartrateData8 = "07 44 00";

  private static PolarH7 polarh7;

  private static HeartRateSample hrs1;
  private static HeartRateSample hrs2;
  private static HeartRateSample hrs3;
  private static HeartRateSample hrs4;
  private static HeartRateSample hrs5;
  private static HeartRateSample hrs6;
  private static HeartRateSample hrs7;
  private static HeartRateSample hrs8;

  @BeforeClass
  public static void setup() {
    polarh7 = new PolarH7(SensorName.PolarH7, PolarH7lib.DEFAULT_BDADDRESS, new JSONObject());
    hrs1 = polarh7.calculateHeartRateData("2016-09-02T08:45:30.555Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData1);
    hrs2 = polarh7.calculateHeartRateData("2016-09-01T23:12:55.378Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData2);
    hrs3 = polarh7.calculateHeartRateData("2016-08-31T15:59:00.004Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData3);
    hrs4 = polarh7.calculateHeartRateData("2016-08-30T01:01:01.010Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData4);
    hrs5 = polarh7.calculateHeartRateData("2016-08-29T20:20:23.299Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData5);
    hrs6 = polarh7.calculateHeartRateData("2016-08-28T11:30:40.990Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData6);
    hrs7 = polarh7.calculateHeartRateData("2016-08-27T16:10:57.083Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData7);
    hrs8 = polarh7.calculateHeartRateData("2016-08-26T22:20:16.224Z", polarh7.gattLibrary.getHandleHeartRateMeasurement(), heartrateData8);
  }

  @Test
  public void testSensorParameters() {
    Assert.assertEquals(AddressType.PUBLIC, PolarH7Test.polarh7.getAddressType());
    Assert.assertEquals(SecurityLevel.LOW, PolarH7Test.polarh7.getSecurityLevel());
  }

  @Test
  public void testHandles() {
    Assert.assertEquals("0x0003", PolarH7Test.polarh7.gattLibrary.getHandleDeviceName());
    Assert.assertEquals("0x0012", PolarH7Test.polarh7.gattLibrary.getHandleHeartRateMeasurement());
    Assert.assertEquals("0x0013", PolarH7Test.polarh7.gattLibrary.getHandleHeartRateNotification());
  }

  @Test
  public void testHeartRateControlByte() {
    // heartrateData1
    Assert.assertFalse(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData1));
    Assert.assertTrue(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData1));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData1));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData1));
    Assert.assertTrue(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData1));
    // heartrateData2
    Assert.assertFalse(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData2));
    Assert.assertTrue(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData2));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData2));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData2));
    Assert.assertFalse(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData2));
    // heartrateData3
    Assert.assertFalse(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData3));
    Assert.assertFalse(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData3));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData3));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData3));
    Assert.assertTrue(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData3));
    // heartrateData4
    Assert.assertFalse(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData4));
    Assert.assertFalse(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData4));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData4));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData4));
    Assert.assertFalse(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData4));
    // heartrateData5
    Assert.assertTrue(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData5));
    Assert.assertTrue(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData5));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData5));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData5));
    Assert.assertTrue(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData5));
    // heartrateData6
    Assert.assertTrue(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData6));
    Assert.assertTrue(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData6));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData6));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData6));
    Assert.assertFalse(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData6));
    // heartrateData7
    Assert.assertTrue(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData7));
    Assert.assertFalse(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData7));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData7));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData7));
    Assert.assertTrue(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData7));
    // heartrateData8
    Assert.assertTrue(PolarH7Test.polarh7.is16BitHeartRateValue(PolarH7Test.heartrateData8));
    Assert.assertFalse(PolarH7Test.polarh7.isEnergyExpendedPresent(PolarH7Test.heartrateData8));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetectionSupported(PolarH7Test.heartrateData8));
    Assert.assertTrue(PolarH7Test.polarh7.isSkinContactDetected(PolarH7Test.heartrateData8));
    Assert.assertFalse(PolarH7Test.polarh7.isRRintervalDataAvailable(PolarH7Test.heartrateData8));
  }

  @Test
  public void testHeartRateValue() {
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs1.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs2.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs3.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs4.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs5.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs6.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs7.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), PolarH7Test.hrs8.getHeartRate());
  }

  @Test
  public void testEnergyExpendedValue() {
    Assert.assertEquals(Integer.valueOf(100), PolarH7Test.hrs1.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), PolarH7Test.hrs2.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), PolarH7Test.hrs5.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), PolarH7Test.hrs6.getEnergyExpended());
  }

  @Test
  public void testRRintervalValue() {
    Assert.assertEquals("[901.3672, 863.28125]", PolarH7Test.hrs1.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", PolarH7Test.hrs3.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", PolarH7Test.hrs5.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", PolarH7Test.hrs7.getRRintervals());
  }

}
