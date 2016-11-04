package de.fhg.fit.biomos.sensorplatform.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

/**
 * Test cases for mapping between database and POJO classes. Testing of database queries. This test backups the old database (directory copy) and is working on
 * an empty one. After tests, the backup database is restored. It is recommended to run this test with an empty database.
 *
 * @author Daniel Pyka
 *
 */
public class DatabaseTest {

  private static final File db = new File("db");
  private static final File dbBackup = new File("dbBackup");

  private static DBcontroller dbc;

  @BeforeClass
  public static void setup() {
    try {
      FileUtils.copyDirectory(db, dbBackup);
    } catch (IOException e) {
      e.printStackTrace();
    }
    dbc = new DBcontroller();
    setupHRS();
    setupPOS();
  }

  private static void setupHRS() {
    DBsession s = dbc.getSession();
    s.deleteAllHeartRateSamples();
    s.commit();
    s.close();
    HeartRateSample hrs1 = new HeartRateSample("2016-09-01T08:45:30.555Z", "11:22:33:44:55:66", false);
    hrs1.setFirstname("TestFirstName");
    hrs1.setLastname("TestLastName");
    hrs1.setHeartRate(255);
    hrs1.setEnergyExpended(0);
    hrs1.setRRintervals(Arrays.asList(new Float[] { 235.3f, 235.3f, 235.3f, 235.3f }));
    hrs1.setTransmitted(true);
    HeartRateSample hrs2 = new HeartRateSample("2016-08-31T08:45:30.555Z", "11:22:33:44:55:66", false);
    hrs2.setFirstname("TestFirstName");
    hrs2.setLastname("TestLastName");
    hrs2.setHeartRate(120);
    hrs2.setEnergyExpended(0);
    hrs2.setRRintervals(Arrays.asList(new Float[] { 500.0f, 500.0f }));
    hrs2.setTransmitted(false);
    HeartRateSample hrs3 = new HeartRateSample("2016-08-30T08:45:30.555Z", "11:22:33:44:55:66", false);
    hrs3.setFirstname("TestFirstName");
    hrs3.setLastname("TestLastName");
    hrs3.setHeartRate(255);
    hrs3.setEnergyExpended(0);
    hrs3.setRRintervals(Arrays.asList(new Float[] { 235.3f, 235.3f, 235.3f, 235.3f }));
    hrs3.setTransmitted(false);
    HeartRateSample hrs4 = new HeartRateSample("2016-08-29T08:45:30.555Z", "11:22:33:44:55:66", false);
    hrs4.setFirstname("TestFirstName");
    hrs4.setLastname("TestLastName");
    hrs4.setHeartRate(60);
    hrs4.setEnergyExpended(0);
    hrs4.setRRintervals(Arrays.asList(new Float[] { 1000.0f }));
    hrs4.setTransmitted(true);
    HeartRateSample hrs5 = new HeartRateSample("2016-08-28T08:45:30.555Z", "11:22:33:44:55:66", false);
    hrs5.setFirstname("TestFirstName");
    hrs5.setLastname("TestLastName");
    hrs5.setHeartRate(240);
    hrs5.setEnergyExpended(0);
    hrs5.setRRintervals(Arrays.asList(new Float[] { 250.0f, 250.0f, 250.0f, 250.0f }));

    hrs5.setTransmitted(true);
    s = dbc.getSession();
    s.saveHeartRateSample(hrs1);
    s.saveHeartRateSample(hrs2);
    s.saveHeartRateSample(hrs3);
    s.saveHeartRateSample(hrs4);
    s.saveHeartRateSample(hrs5);
    s.commit();
    s.close();
  }

  private static void setupPOS() {
    DBsession s = dbc.getSession();
    s.deleteAllPulseOximeterSamples();
    s.commit();
    s.close();
    PulseOximeterSample pos1 = new PulseOximeterSample("2016-08-27T08:45:30.555Z", "66:55:44:33:22:11");
    pos1.setFirstname("TestFirstName");
    pos1.setLastname("TestLastName");
    pos1.setPulseRate(60);
    pos1.setSpO2(95);
    PulseOximeterSample pos2 = new PulseOximeterSample("2016-08-26T08:45:30.555Z", "66:55:44:33:22:11");
    pos2.setFirstname("TestFirstName");
    pos2.setLastname("TestLastName");
    pos2.setPulseRate(120);
    pos2.setSpO2(96);
    PulseOximeterSample pos3 = new PulseOximeterSample("2016-08-25T08:45:30.555Z", "66:55:44:33:22:11");
    pos3.setFirstname("TestFirstName");
    pos3.setLastname("TestLastName");
    pos3.setPulseRate(180);
    pos3.setSpO2(97);
    PulseOximeterSample pos4 = new PulseOximeterSample("2016-08-24T08:45:30.555Z", "66:55:44:33:22:11");
    pos4.setFirstname("TestFirstName");
    pos4.setLastname("TestLastName");
    pos4.setPulseRate(240);
    pos4.setSpO2(98);
    PulseOximeterSample pos5 = new PulseOximeterSample("2016-08-23T08:45:30.555Z", "66:55:44:33:22:11");
    pos5.setFirstname("TestFirstName");
    pos5.setLastname("TestLastName");
    pos5.setPulseRate(90);
    pos5.setSpO2(99);
    s = dbc.getSession();
    s.savePulseOximeterSample(pos1);
    s.savePulseOximeterSample(pos2);
    s.savePulseOximeterSample(pos3);
    s.savePulseOximeterSample(pos4);
    s.savePulseOximeterSample(pos5);
    s.commit();
    s.close();
  }

  @AfterClass
  public static void shutdown() {
    DBsession s = DatabaseTest.dbc.getSession();
    s.deleteAllHeartRateSamples();
    s.deleteAllPulseOximeterSamples();
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    s.shutdown();
    s.close();
    DatabaseTest.dbc.close();
    try {
      FileUtils.deleteDirectory(DatabaseTest.db);
      FileUtils.copyDirectory(DatabaseTest.dbBackup, DatabaseTest.db);
      FileUtils.deleteDirectory(DatabaseTest.dbBackup);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testHRS() {
    DBsession s = DatabaseTest.dbc.getSession();
    Assert.assertEquals(5, s.getNumberOfHeartRateSamples());
    List<HeartRateSample> hrss = s.getHeartRateSamples();
    Assert.assertEquals(5, hrss.size());
    Assert.assertEquals(2, s.getNumberOfNotTransmittedHeartRateSamples());
    List<HeartRateSample> notTransmittedHrs = s.getNotTransmittedHeartRateSamples();
    Assert.assertEquals(2, notTransmittedHrs.size());
    for (HeartRateSample heartRateSample : notTransmittedHrs) {
      Assert.assertFalse(heartRateSample.isTransmitted());
    }
    HeartRateSample hrs = hrss.get(0);
    Assert.assertEquals("11:22:33:44:55:66", hrs.getBDaddress());
    Assert.assertEquals("2016-09-01T08:45:30.555Z", hrs.getTimestamp());
    Assert.assertEquals(Integer.valueOf(255), hrs.getHeartRate());
    Assert.assertEquals(Integer.valueOf(255), hrs.getHeartRate());
    Assert.assertEquals(Integer.valueOf(0), hrs.getEnergyExpended());
    Assert.assertEquals("[235.3, 235.3, 235.3, 235.3]", hrs.getRRintervals());
    Assert.assertEquals("TestFirstName", hrs.getFirstname());
    Assert.assertEquals("TestLastName", hrs.getLastname());
    s.close();
  }

  @Test
  public void testPOS() {
    DBsession s = DatabaseTest.dbc.getSession();
    List<PulseOximeterSample> poss = s.getPulseOximeterSamples();
    Assert.assertEquals(5, poss.size());
    PulseOximeterSample pos = poss.get(2);
    Assert.assertEquals(Integer.valueOf(180), pos.getPulseRate());
    Assert.assertEquals(Integer.valueOf(97), pos.getSpO2());
    Assert.assertEquals("TestFirstName", pos.getFirstname());
    Assert.assertEquals("TestLastName", pos.getLastname());
    s.close();
  }

  @Test
  public void testCC2650TemperatureSample() {
    CC2650TemperatureSample sample = new CC2650TemperatureSample("2016-08-22T08:45:30.555Z", "AA:BB:CC:DD:EE:FF");
    sample.setFirstname("TestFirstName");
    sample.setLastname("TestLastName");
    sample.setDieTemperature(29.9f);
    sample.setObjectTemperature(35.7f);
    DBsession s = DatabaseTest.dbc.getSession();
    s.saveCC2650TemperatureSample(sample);
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    List<CC2650TemperatureSample> ss = s.getCC2650TemperatureSamples();
    Assert.assertEquals(1, ss.size());
    sample = ss.get(0);
    Assert.assertEquals("2016-08-22T08:45:30.555Z", sample.getTimestamp());
    Assert.assertEquals("AA:BB:CC:DD:EE:FF", sample.getBdAddress());
    Assert.assertEquals(Float.valueOf(29.9f), sample.getDieTemperature());
    Assert.assertEquals(Float.valueOf(35.7f), sample.getObjectTemperature());
    Assert.assertEquals("TestFirstName", sample.getFirstname());
    Assert.assertEquals("TestLastName", sample.getLastname());
    s.close();
  }

  @Test
  public void testCC2650HumiditySample() {
    CC2650HumiditySample sample = new CC2650HumiditySample("2016-08-21T08:45:30.555Z", "AA:BB:CC:DD:EE:FF");
    sample.setFirstname("TestFirstName");
    sample.setLastname("TestLastName");
    sample.setTemperature(32.2f);
    sample.setHumidity(58.3f);
    DBsession s = DatabaseTest.dbc.getSession();
    s.saveCC2650HumiditySample(sample);
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    List<CC2650HumiditySample> ss = s.getCC2650HumiditySamples();
    Assert.assertEquals(1, ss.size());
    sample = ss.get(0);
    Assert.assertEquals("2016-08-21T08:45:30.555Z", sample.getTimestamp());
    Assert.assertEquals("AA:BB:CC:DD:EE:FF", sample.getBdAddress());
    Assert.assertEquals(Float.valueOf(32.2f), sample.getTemperature());
    Assert.assertEquals(Float.valueOf(58.3f), sample.getHumidity());
    Assert.assertEquals("TestFirstName", sample.getFirstname());
    Assert.assertEquals("TestLastName", sample.getLastname());
    s.close();
  }

  @Test
  public void testCC2650PressureSample() {
    CC2650PressureSample sample = new CC2650PressureSample("2016-08-20T08:45:30.555Z", "AA:BB:CC:DD:EE:FF");
    sample.setFirstname("TestFirstName");
    sample.setLastname("TestLastName");
    sample.setTemperature(25.1f);
    sample.setPressure(1002.4f);
    DBsession s = DatabaseTest.dbc.getSession();
    s.saveCC2650PressureSample(sample);
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    List<CC2650PressureSample> ss = s.getCC2650PressureSamples();
    Assert.assertEquals(1, ss.size());
    sample = ss.get(0);
    Assert.assertEquals("2016-08-20T08:45:30.555Z", sample.getTimestamp());
    Assert.assertEquals("AA:BB:CC:DD:EE:FF", sample.getBdAddress());
    Assert.assertEquals(Float.valueOf(25.1f), sample.getTemperature());
    Assert.assertEquals(Float.valueOf(1002.4f), sample.getPressure());
    Assert.assertEquals("TestFirstName", sample.getFirstname());
    Assert.assertEquals("TestLastName", sample.getLastname());
    s.close();
  }

  @Test
  public void testCC2650AmbientlightSample() {
    CC2650AmbientlightSample sample = new CC2650AmbientlightSample("2016-08-19T08:45:30.555Z", "AA:BB:CC:DD:EE:FF");
    sample.setFirstname("TestFirstName");
    sample.setLastname("TestLastName");
    sample.setAmbientlight(555.5f);
    DBsession s = DatabaseTest.dbc.getSession();
    s.saveCC2650AmbientlightSample(sample);
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    List<CC2650AmbientlightSample> ss = s.getCC2650AmbientlightSamples();
    Assert.assertEquals(1, ss.size());
    sample = ss.get(0);
    Assert.assertEquals("2016-08-19T08:45:30.555Z", sample.getTimestamp());
    Assert.assertEquals("AA:BB:CC:DD:EE:FF", sample.getBdAddress());
    Assert.assertEquals(Float.valueOf(555.5f), sample.getAmbientlight());
    Assert.assertEquals("TestFirstName", sample.getFirstname());
    Assert.assertEquals("TestLastName", sample.getLastname());
    s.close();
  }

  @Test
  public void testCC2650MovementSample() {
    CC2650MovementSample sample = new CC2650MovementSample("2016-08-18T08:45:30.555Z", "AA:BB:CC:DD:EE:FF");
    sample.setFirstname("TestFirstName");
    sample.setLastname("TestLastName");
    sample.setRotationX(1.7709924f);
    sample.setRotationY(0.4351145f);
    sample.setRotationZ(-1.4580153f);
    sample.setAccelerationX(0.22265625f);
    sample.setAccelerationY(0.024414062f);
    sample.setAccelerationZ(1.6855469f);
    sample.setMagnetismX(-246.0f);
    sample.setMagnetismY(653.0f);
    sample.setMagnetismZ(-88.0f);
    DBsession s = DatabaseTest.dbc.getSession();
    s.saveCC2650MovementSample(sample);
    s.commit();
    s.close();
    s = DatabaseTest.dbc.getSession();
    List<CC2650MovementSample> ss = s.getCC2650MovementSamples();
    Assert.assertEquals(1, ss.size());
    sample = ss.get(0);
    Assert.assertEquals("2016-08-18T08:45:30.555Z", sample.getTimestamp());
    Assert.assertEquals("AA:BB:CC:DD:EE:FF", sample.getBdAddress());
    Assert.assertEquals(Float.valueOf(1.7709924f), sample.getRotationX());
    Assert.assertEquals(Float.valueOf(0.4351145f), sample.getRotationY());
    Assert.assertEquals(Float.valueOf(-1.4580153f), sample.getRotationZ());
    Assert.assertEquals(Float.valueOf(0.22265625f), sample.getAccelerationX());
    Assert.assertEquals(Float.valueOf(0.024414062f), sample.getAccelerationY());
    Assert.assertEquals(Float.valueOf(1.6855469f), sample.getAccelerationZ());
    Assert.assertEquals(Float.valueOf(-246.0f), sample.getMagnetismX());
    Assert.assertEquals(Float.valueOf(653.0f), sample.getMagnetismY());
    Assert.assertEquals(Float.valueOf(-88.0f), sample.getMagnetismZ());
    Assert.assertEquals("TestFirstName", sample.getFirstname());
    Assert.assertEquals("TestLastName", sample.getLastname());
    s.close();
  }
}
