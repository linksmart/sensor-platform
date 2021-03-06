package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sensor.PolarH7;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Test for sensor commands enable and disable notifications. Instead of creating a process und using its stream, a ByteArrayOutputStream is used to capture the
 * content.
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolImplTest {

  private PolarH7 polarh7;

  @Before
  public void setup() {
    this.polarh7 = new PolarH7(SensorName.PolarH7, PolarH7lib.DEFAULT_BDADDRESS, new JSONObject());
  }

  @Test
  public void testStreamToSensor() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedWriter streamToSensor = new BufferedWriter(new OutputStreamWriter(baos));

    this.polarh7.enableDataNotification(streamToSensor, GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    Assert.assertEquals("char-write-cmd 0x0013 01:00", new String(baos.toByteArray()).replace("\n", "").replace("\r", ""));
    baos.reset();

    this.polarh7.disableDataNotification(streamToSensor, GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
    Assert.assertEquals("char-write-cmd 0x0013 00:00", new String(baos.toByteArray()).replace("\n", "").replace("\r", ""));
    baos.reset();
  }

}
