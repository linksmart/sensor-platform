package de.fhg.fit.biomos.sensorplatform.sensor;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Test {

  private CC2650 cc2650;

  @Before
  public void setup() {
    JSONObject config = new JSONObject();
    config.put("irtemperature", "64");
    config.put("humidity", "64");
    config.put("ambientlight", "50");
    config.put("pressure", "64");
    config.put("movement", "64");
    this.cc2650 = new CC2650(SensorName.CC2650, CC2650lib.DEFAULT_BDADDRESS, config);
  }

  @Test
  public void testHandles() {
    Assert.assertEquals("0x0021", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_VALUE);
    Assert.assertEquals("0x0022", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_NOTIFICATION);
    Assert.assertEquals("0x0024", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_ENABLE);
    Assert.assertEquals("0x0026", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_PERIOD);

    Assert.assertEquals("0x0029", this.cc2650.gattLibrary.HANDLE_HUMIDITY_VALUE);
    Assert.assertEquals("0x002a", this.cc2650.gattLibrary.HANDLE_HUMIDITY_NOTIFICATION);
    Assert.assertEquals("0x002c", this.cc2650.gattLibrary.HANDLE_HUMIDITY_ENABLE);
    Assert.assertEquals("0x002e", this.cc2650.gattLibrary.HANDLE_HUMIDITY_PERIOD);

    Assert.assertEquals("0x0031", this.cc2650.gattLibrary.HANDLE_PRESSURE_VALUE);
    Assert.assertEquals("0x0032", this.cc2650.gattLibrary.HANDLE_PRESSURE_NOTIFICATION);
    Assert.assertEquals("0x0034", this.cc2650.gattLibrary.HANDLE_PRESSURE_ENABLE);
    Assert.assertEquals("0x0036", this.cc2650.gattLibrary.HANDLE_PRESSURE_PERIOD);

    Assert.assertEquals("0x0041", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_VALUE);
    Assert.assertEquals("0x0042", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_NOTIFICATION);
    Assert.assertEquals("0x0044", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_ENABLE);
    Assert.assertEquals("0x0046", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_PERIOD);

    Assert.assertEquals("0x0039", this.cc2650.gattLibrary.HANDLE_MOVEMENT_VALUE);
    Assert.assertEquals("0x003a", this.cc2650.gattLibrary.HANDLE_MOVEMENT_NOTIFICATION);
    Assert.assertEquals("0x003c", this.cc2650.gattLibrary.HANDLE_MOVEMENT_ENABLE);
    Assert.assertEquals("0x003e", this.cc2650.gattLibrary.HANDLE_MOVEMENT_PERIOD);

    // TODO add handles for enable movement
  }

  @Test
  public void testIRtemperature() {
    String data = "40 0a f0 0d";
    CC2650TemperatureSample sample = this.cc2650.calculateTemperatureData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_VALUE,
        data.replace(" ", ""));
    Assert.assertEquals(Float.valueOf(20.5f), sample.getObjectTemperature());
    Assert.assertEquals(Float.valueOf(27.875f), sample.getDieTemperature());

    Assert.assertEquals(null,
        this.cc2650.calculateTemperatureData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_MOVEMENT_VALUE, data.replace(" ", "")));
  }

  @Test
  public void testHumidity() {
    String data = "64 6b 38 5c";
    CC2650HumiditySample sample = this.cc2650.calculateHumidityData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_HUMIDITY_VALUE,
        data.replace(" ", ""));
    Assert.assertEquals(Float.valueOf(29.216614f), sample.getTemperature());
    Assert.assertEquals(Float.valueOf(36.02295f), sample.getHumidity());

    Assert.assertEquals(null,
        this.cc2650.calculateHumidityData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_VALUE, data.replace(" ", "")));
  }

  @Test
  public void testPressure() {
    String data = "bd 0b 00 f6 87 01";
    CC2650PressureSample sample = this.cc2650.calculatePressureData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_PRESSURE_VALUE,
        data.replace(" ", ""));
    Assert.assertEquals(Float.valueOf(30.05f), sample.getTemperature());
    Assert.assertEquals(Float.valueOf(1003.42f), sample.getPressure());

    Assert.assertEquals(null,
        this.cc2650.calculatePressureData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_IR_TEMPERATURE_VALUE, data.replace(" ", "")));
  }

  @Test
  public void testAmbientlight() {
    String data = "9e 3e";
    CC2650AmbientlightSample sample = this.cc2650.calculateAmbientlightData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_VALUE,
        data.replace(" ", ""));
    Assert.assertEquals(Float.valueOf(299.36f), sample.getAmbientlight());
    Assert.assertEquals(null,
        this.cc2650.calculatePressureData("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_HUMIDITY_VALUE, data.replace(" ", "")));
  }

  @Test
  public void testMovement() {
    String data = "e8 00 39 00 41 ff c8 01 32 00 7c 0d 0a ff 8d 02 a8 ff";
    CC2650MovementSample sample = this.cc2650.calculateMovementSample("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_MOVEMENT_VALUE,
        data.replace(" ", ""));
    Assert.assertEquals(Float.valueOf(1.7709924f), sample.getRotationX());
    Assert.assertEquals(Float.valueOf(0.4351145f), sample.getRotationY());
    Assert.assertEquals(Float.valueOf(-1.4580153f), sample.getRotationZ());
    Assert.assertEquals(Float.valueOf(0.22265625f), sample.getAccelerationX());
    Assert.assertEquals(Float.valueOf(0.024414062f), sample.getAccelerationY());
    Assert.assertEquals(Float.valueOf(1.6855469f), sample.getAccelerationZ());
    Assert.assertEquals(Float.valueOf(-246.0f), sample.getMagnetismX());
    Assert.assertEquals(Float.valueOf(653.0f), sample.getMagnetismY());
    Assert.assertEquals(Float.valueOf(-88.0f), sample.getMagnetismZ());
    Assert.assertEquals(null,
        this.cc2650.calculateMovementSample("2016-09-02T08:45:30.555Z", this.cc2650.gattLibrary.HANDLE_AMBIENTLIGHT_VALUE, data.replace(" ", "")));
  }

}
