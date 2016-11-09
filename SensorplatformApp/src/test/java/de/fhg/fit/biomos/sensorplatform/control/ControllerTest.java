package de.fhg.fit.biomos.sensorplatform.control;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.fhg.fit.biomos.sensorplatform.guice.SensorplatformServletConfig;
import de.fhg.fit.biomos.sensorplatform.sensor.PolarH7;
import de.fhg.fit.biomos.sensorplatform.sensor.AbstractSensor;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;
import de.fhg.fit.biomos.sensorplatform.system.RaspberryPi3;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.web.ServerStarter;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;
import de.fhg.fit.biomos.sensorplatform.web.WebHrsUploader;

/**
 * Test for Dependency Injection, SensorWrapperFactory and Controller. The tests can only run succesfully on a Linux machine with Bluez installed!! In order to
 * run this test, you need a Polar H7 sensor which is available during test execution (such that gatttool is able to connect).
 *
 * @author Daniel Pyka
 *
 */
@Ignore
public class ControllerTest {

  private static Properties properties = new Properties();
  private static Injector injector;
  private static ServerStarter serverstarter;

  private static JSONArray configuration;
  private static List<AbstractSensorWrapper<?>> aswList;

  @BeforeClass
  public static void setup() {
    try {
      properties.load(ClassLoader.getSystemResourceAsStream("SensorplatformApp.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    properties.put("version", "0.1-SNAPSHOT");
    properties.put("target.platform", "raspberrypi3");
    properties.put("webapp.port", "8080");
    properties.put("keystore.filename", "keystore.jks");
    properties.put("keystore.password", "123456");
    properties.put("webapp.username", "sensorplatform");
    properties.put("webapp.password", "123456");
    properties.put("http.useragent.boardname", "Junit test");
    properties.put("webinterface.name", "WebHrs");
    properties.put("webinterface.data.url", "http://webhrs.ddnss.de:12345/hrs/upload");
    properties.put("webinterface.timestamp.format", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");

    SensorplatformServletConfig sensorplatformServletConfig = new SensorplatformServletConfig(properties);
    serverstarter = new ServerStarter(properties, sensorplatformServletConfig);
    serverstarter.start();
    injector = sensorplatformServletConfig.getCreatedInjector();

    configuration = new JSONArray();
    JSONObject sensor1 = new JSONObject();
    sensor1.put("name", "PolarH7");
    sensor1.put("bdaddress", "11:22:33:44:55:66");
    sensor1.put("settings", new JSONObject());
    configuration.put(sensor1);
  }

  @Test
  public void testBindings() {
    Assert.assertTrue(injector.getInstance(Uploader.class) instanceof WebHrsUploader);
    Assert.assertTrue(injector.getInstance(HardwarePlatform.class) instanceof RaspberryPi3);
  }

  @Test
  public void testSensorWrapperFactory() {
    SensorWrapperFactory swFactory = injector.getInstance(SensorWrapperFactory.class);
    aswList = swFactory.createSensorWrapper(configuration, "TestFirstName", "TestLastName");
    AbstractSensor<?> sensor = aswList.get(0).getSensor();
    Gatttool gatttool = aswList.get(0).getGatttool();

    Assert.assertTrue(sensor instanceof PolarH7);
    Assert.assertEquals(SensorName.PolarH7, sensor.getName());
    Assert.assertEquals("11:22:33:44:55:66", sensor.getBDaddress());
    Assert.assertEquals(AddressType.PUBLIC, sensor.getAddressType());
    Assert.assertEquals(SecurityLevel.LOW, sensor.getSecurityLevel());
    Assert.assertEquals("{}", sensor.getSettings().toString());

    Assert.assertEquals(Gatttool.State.DISCONNECTED, gatttool.getInternalState());
  }

  @Test
  public void testControllerWorkflow() {
    Controller controller = injector.getInstance(Controller.class);
    Assert.assertFalse(controller.isRecording());
    controller.startRecordingPeriod(10000, "TestFirstName", "TestLastName", configuration, true);
    Assert.assertTrue(controller.isRecording());
    Assert.assertTrue(new File("recording.properties").exists());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    controller.interruptController();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Assert.assertFalse(controller.isRecording());
    Assert.assertFalse(new File("recording.properties").exists());
  }

  @AfterClass
  public static void cleanup() {
    serverstarter.stop();
  }

}
