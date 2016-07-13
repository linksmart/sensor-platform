package de.fhg.fit.biomos.sensorplatform.sensors;

import java.util.Properties;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.fit.biomos.sensorplatform.gatt.PolarH7lib;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

@Ignore
public class PolarH7Test {

  private Sensor sensor;

  @Before
  public void createSensor() {
    Properties properties = new Properties();

    SensorName name = SensorName.PolarH7;
    String bdAddress = PolarH7lib.DEFAULT_BDADDRESS;
    AddressType addressType = AddressType.PUBLIC;

    JSONObject sensorConfiguration = new JSONObject();
    // TODO

    this.sensor = new PolarH7(properties, name, bdAddress, addressType, sensorConfiguration);
  }

  @Test
  public void calculateHeartRates() {
  }
}
