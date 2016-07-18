package de.fhg.biomos.sensorplatform.sensors;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorConfiguration;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

@Ignore
public class PolarH7Test {

  private Sensor sensor;

  private final ByteArrayOutputStream sysout = new ByteArrayOutputStream();
  private final ByteArrayOutputStream syserr = new ByteArrayOutputStream();

  @Before
  public void createSensor() {
    // System.setOut(new PrintStream(this.sysout));
    // System.setErr(new PrintStream(this.syserr));

    Properties properties = new Properties();
    properties.put("logfile.timestamp.format", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");

    SensorName name = SensorName.PolarH7;
    String bdAddress = PolarH7lib.DEFAULT_BDADDRESS;
    AddressType addressType = AddressType.PUBLIC;

    JSONObject sensorConfiguration = new JSONObject();

    JSONObject logging = new JSONObject();
    logging.put(SensorConfiguration.CONSOLE, true);
    logging.put(SensorConfiguration.FILE, false);
    logging.put(SensorConfiguration.WEBINTERFACE, "");

    JSONObject measures = new JSONObject();

    sensorConfiguration.put("logging", logging);
    sensorConfiguration.put("measures", measures);

    this.sensor = new PolarH7(properties, name, bdAddress, addressType, sensorConfiguration);
  }

  @Test
  public void calculateHeartRates() {
    this.sensor.processSensorData(PolarH7lib.HANDLE_HEART_RATE_MEASUREMENT, "16 00 4c 14 03 01");
    Assert.assertTrue("test".equals(this.sysout.toString()));
  }

  // @After
  // public void cleanUpStreams() {
  // System.setOut(null);
  // System.setErr(null);
  // }
}
