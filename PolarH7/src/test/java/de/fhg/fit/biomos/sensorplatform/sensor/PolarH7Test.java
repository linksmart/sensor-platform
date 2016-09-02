package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
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
  private final String heartrateData1 = "1E 44 64 00 9b 03 74 03";
  // CC HH EE EE
  private final String heartrateData2 = "0E 44 64 00 9b";
  // CC HH RR RR RR RR
  private final String heartrateData3 = "16 44 9b 03 74 03";
  // CC HH
  private final String heartrateData4 = "06 44";
  // CC HH HH EE EE RR RR RR RR
  private final String heartrateData5 = "1F 44 00 64 00 9b 03 74 03";
  // CC HH HH EE EE
  private final String heartrateData6 = "0F 44 00 64 00";
  // CC HH HH RR RR RR RR
  private final String heartrateData7 = "17 44 00 9b 03 74 03";
  // CC HH HH
  private final String heartrateData8 = "07 44 00";

  private PolarH7 polarh7;

  private HeartRateSample hrs1;
  private HeartRateSample hrs2;
  private HeartRateSample hrs3;
  private HeartRateSample hrs4;
  private HeartRateSample hrs5;
  private HeartRateSample hrs6;
  private HeartRateSample hrs7;
  private HeartRateSample hrs8;

  @Before
  public void setup() {
    this.polarh7 = new PolarH7(SensorName.PolarH7, PolarH7lib.DEFAULT_BDADDRESS, new JSONObject());
    this.hrs1 = this.polarh7.calculateHeartRateData("2016-09-02T08:45:30.555Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData1);
    this.hrs2 = this.polarh7.calculateHeartRateData("2016-09-01T23:12:55.378Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData2);
    this.hrs3 = this.polarh7.calculateHeartRateData("2016-08-31T15:59:00.004Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData3);
    this.hrs4 = this.polarh7.calculateHeartRateData("2016-08-30T01:01:01.010Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData4);
    this.hrs5 = this.polarh7.calculateHeartRateData("2016-08-29T20:20:23.299Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData5);
    this.hrs6 = this.polarh7.calculateHeartRateData("2016-08-28T11:30:40.990Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData6);
    this.hrs7 = this.polarh7.calculateHeartRateData("2016-08-27T16:10:57.083Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData7);
    this.hrs8 = this.polarh7.calculateHeartRateData("2016-08-26T22:20:16.224Z", this.polarh7.gattLibrary.getHandleHeartRateMeasurement(), this.heartrateData8);
  }

  @Test
  public void testSensorParameters() {
    Assert.assertEquals(AddressType.PUBLIC, this.polarh7.getAddressType());
    Assert.assertEquals(SecurityLevel.LOW, this.polarh7.getSecurityLevel());
  }

  @Test
  public void testHandles() {
    Assert.assertEquals("0x0003", this.polarh7.gattLibrary.getHandleDeviceName());
    Assert.assertEquals("0x0012", this.polarh7.gattLibrary.getHandleHeartRateMeasurement());
    Assert.assertEquals("0x0013", this.polarh7.gattLibrary.getHandleHeartRateNotification());
  }

  @Test
  public void testHeartRateControlByte() {
    // heartrateData1
    Assert.assertFalse(this.polarh7.is16BitHeartRateValue(this.heartrateData1));
    Assert.assertTrue(this.polarh7.isEnergyExpendedPresent(this.heartrateData1));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData1));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData1));
    Assert.assertTrue(this.polarh7.isRRintervalDataAvailable(this.heartrateData1));
    // heartrateData2
    Assert.assertFalse(this.polarh7.is16BitHeartRateValue(this.heartrateData2));
    Assert.assertTrue(this.polarh7.isEnergyExpendedPresent(this.heartrateData2));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData2));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData2));
    Assert.assertFalse(this.polarh7.isRRintervalDataAvailable(this.heartrateData2));
    // heartrateData3
    Assert.assertFalse(this.polarh7.is16BitHeartRateValue(this.heartrateData3));
    Assert.assertFalse(this.polarh7.isEnergyExpendedPresent(this.heartrateData3));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData3));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData3));
    Assert.assertTrue(this.polarh7.isRRintervalDataAvailable(this.heartrateData3));
    // heartrateData4
    Assert.assertFalse(this.polarh7.is16BitHeartRateValue(this.heartrateData4));
    Assert.assertFalse(this.polarh7.isEnergyExpendedPresent(this.heartrateData4));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData4));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData4));
    Assert.assertFalse(this.polarh7.isRRintervalDataAvailable(this.heartrateData4));
    // heartrateData5
    Assert.assertTrue(this.polarh7.is16BitHeartRateValue(this.heartrateData5));
    Assert.assertTrue(this.polarh7.isEnergyExpendedPresent(this.heartrateData5));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData5));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData5));
    Assert.assertTrue(this.polarh7.isRRintervalDataAvailable(this.heartrateData5));
    // heartrateData6
    Assert.assertTrue(this.polarh7.is16BitHeartRateValue(this.heartrateData6));
    Assert.assertTrue(this.polarh7.isEnergyExpendedPresent(this.heartrateData6));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData6));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData6));
    Assert.assertFalse(this.polarh7.isRRintervalDataAvailable(this.heartrateData6));
    // heartrateData7
    Assert.assertTrue(this.polarh7.is16BitHeartRateValue(this.heartrateData7));
    Assert.assertFalse(this.polarh7.isEnergyExpendedPresent(this.heartrateData7));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData7));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData7));
    Assert.assertTrue(this.polarh7.isRRintervalDataAvailable(this.heartrateData7));
    // heartrateData8
    Assert.assertTrue(this.polarh7.is16BitHeartRateValue(this.heartrateData8));
    Assert.assertFalse(this.polarh7.isEnergyExpendedPresent(this.heartrateData8));
    Assert.assertTrue(this.polarh7.isSkinContactDetectionSupported(this.heartrateData8));
    Assert.assertTrue(this.polarh7.isSkinContactDetected(this.heartrateData8));
    Assert.assertFalse(this.polarh7.isRRintervalDataAvailable(this.heartrateData8));
  }

  @Test
  public void testHeartRateValue() {
    Assert.assertEquals(Integer.valueOf(68), this.hrs1.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs2.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs3.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs4.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs5.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs6.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs7.getHeartRate());
    Assert.assertEquals(Integer.valueOf(68), this.hrs8.getHeartRate());
  }

  @Test
  public void testEnergyExpendedValue() {
    Assert.assertEquals(Integer.valueOf(100), this.hrs1.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), this.hrs2.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), this.hrs5.getEnergyExpended());
    Assert.assertEquals(Integer.valueOf(100), this.hrs6.getEnergyExpended());
  }

  @Test
  public void testRRintervalValue() {
    Assert.assertEquals("[901.3672, 863.28125]", this.hrs1.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", this.hrs3.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", this.hrs5.getRRintervals());
    Assert.assertEquals("[901.3672, 863.28125]", this.hrs7.getRRintervals());
  }

}
