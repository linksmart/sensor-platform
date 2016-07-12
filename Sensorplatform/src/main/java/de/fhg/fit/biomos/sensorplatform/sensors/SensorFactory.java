package de.fhg.fit.biomos.sensorplatform.sensors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    String sensorsConfigurationFile = this.properties.getProperty("sensor.configuration.file");
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
      SensorName name = SensorName.valueOf(sensorDescription.getString("name"));
      String bdAddress = sensorDescription.getString("bdaddress");
      AddressType addressType = AddressType.valueOf(sensorDescription.getString("addresstype"));

      JSONObject sensorConfig = sensorDescription.getJSONObject("configuration");
      SensorConfiguration sensorConfiguration = new SensorConfiguration();

      for (Iterator<String> iter = sensorConfig.keys(); iter.hasNext();) {
        String key = iter.next();
        sensorConfiguration.addSetting(key, sensorConfig.getString(key));
      }

      Sensor sensor = null;
      switch (name) {
        case PolarH7:
          sensor = new PolarH7(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        case AdidasHRM:
          sensor = new TomTomAdidas(this.properties, name, bdAddress, addressType, sensorConfiguration);
          break;
        case TomTomHRM:
          sensor = new TomTomAdidas(this.properties, name, bdAddress, addressType, sensorConfiguration);
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
