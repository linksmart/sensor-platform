package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.sensor.AdidasMiCoachHRM;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorConfiguration;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Factory for creating sensor objects. It is recommended to NOT create sensor objects directly but only through the factory.
 *
 * @author Daniel Pyka
 *
 */
public class SensorFactory {

  private static final Logger LOG = LoggerFactory.getLogger(SensorFactory.class);

  private final Properties properties;
  private final JSONArray sensorsConfiguration;

  public SensorFactory(Properties properties) {
    this.properties = properties;
    String sensorsConfigurationFile = this.properties.getProperty("default.sensor.configuration.file");
    JSONTokener tokener = new JSONTokener(ClassLoader.getSystemResourceAsStream(sensorsConfigurationFile));
    this.sensorsConfiguration = new JSONArray(tokener);
    LOG.info("sensor configuration file " + sensorsConfigurationFile);
  }

  /**
   * Creates sensors from a configuration file provided by the maven build process or an "external" file.
   *
   * @return List&lt;Sensor&gt; List of sensors the sensorplatform shall work with
   */
  public List<Sensor> createSensorsFromConfigurationFile() {
    List<Sensor> sensorList = new ArrayList<Sensor>();
    for (int i = 0; i < this.sensorsConfiguration.length(); i++) {
      JSONObject sensorDescription = this.sensorsConfiguration.getJSONObject(i);
      SensorName name = SensorName.valueOf(sensorDescription.getString(SensorConfiguration.NAME));
      String bdAddress = sensorDescription.getString(SensorConfiguration.BDADDRESS);
      AddressType addressType = AddressType.valueOf(sensorDescription.getString(SensorConfiguration.ADDRESSTYPE));

      JSONObject sensorConfiguration = sensorDescription.getJSONObject(SensorConfiguration.CONFIGURATION);

      Sensor sensor = null;
      switch (name) {
        case PolarH7:
          sensor = new PolarH7(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        case AdidasHRM:
          sensor = new AdidasMiCoachHRM(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        case TomTomHRM:
          sensor = new TomTomHRM(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        case PolarV800:
          break;
        case CC2650:
          sensor = new CC2650(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        default:
          LOG.error("unknown sensor name " + name);
          break;
      }

      if (sensor != null) {
        sensorList.add(sensor);
      }
    }
    return sensorList;
  }

}
