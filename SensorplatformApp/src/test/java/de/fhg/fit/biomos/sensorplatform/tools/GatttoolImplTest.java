package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.sensor.PolarH7;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolCmd;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Test for sensor commands enable and disable notifications. Instead of creating a process und using its stream, a ByteArrayOutputStream is used to capture the
 * content.
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolImplTest {

  private static PolarH7 polarh7;

  @BeforeClass
  public static void setup() {
    polarh7 = new PolarH7(SensorName.PolarH7, PolarH7lib.DEFAULT_BDADDRESS, new JSONObject());
  }

  @Test
  public void testStreamToSensor() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedWriter streamToSensor = new BufferedWriter(new OutputStreamWriter(baos));

    GatttoolImplTest.polarh7.enableAllNotification(streamToSensor, GatttoolCmd.CMD_CHAR_WRITE_CMD, GatttoolCmd.ENABLE_NOTIFICATION);
    Assert.assertEquals("char-write-cmd 0x0013 01:00", new String(baos.toByteArray()).replace("\n", "").replace("\r", ""));
    baos.reset();

    GatttoolImplTest.polarh7.disableAllNotification(streamToSensor, GatttoolCmd.CMD_CHAR_WRITE_CMD, GatttoolCmd.DISABLE_NOTIFICATION);
    Assert.assertEquals("char-write-cmd 0x0013 00:00", new String(baos.toByteArray()).replace("\n", "").replace("\r", ""));
    baos.reset();
  }

}
