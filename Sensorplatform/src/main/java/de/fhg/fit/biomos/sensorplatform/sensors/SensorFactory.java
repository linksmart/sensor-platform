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
import de.fhg.fit.biomos.sensorplatform.util.SensorType;

public class SensorFactory {

  private static final Logger LOG = LoggerFactory.getLogger(SensorFactory.class);

  private final Properties properties;
  private final JSONArray sensorsConfiguration;

  public SensorFactory(Properties properties) {
    this.properties = properties;
    String sensorsDescriptionFile = this.properties.getProperty("sensors.description.file");
    JSONTokener tokener = new JSONTokener(ClassLoader.getSystemResourceAsStream(sensorsDescriptionFile));
    this.sensorsConfiguration = new JSONArray(tokener);
    LOG.info("sensor configuration file " + sensorsDescriptionFile);
  }

  public List<Sensor> createSensorsFromConfigurationFile() {
    List<Sensor> sensorList = new ArrayList<Sensor>();
    for (int i = 0; i < this.sensorsConfiguration.length(); i++) {
      JSONObject sensorDescription = this.sensorsConfiguration.getJSONObject(i);
      SensorName name = SensorName.valueOf(sensorDescription.getString("name"));
      String bdAddress = sensorDescription.getString("bdaddress");
      AddressType addressType = AddressType.valueOf(sensorDescription.getString("addresstype"));
      SensorType sensorType = SensorType.valueOf(sensorDescription.getString("sensortype"));

      Sensor sensor = null;
      switch (name) {
        case PolarH7:
          sensor = new PolarH7(this.properties, name, bdAddress, addressType, sensorType);
          break;
        case AdidasHRM:
          break;
        case TomTomHRM:
          break;
        case PolarV800:
          break;
        case CC2650:
          JSONObject sensorConfig = sensorDescription.getJSONObject("configuration");
          SensorConfiguration sensorConfiguration = new SensorConfiguration();

          for (Iterator<String> iter = sensorConfig.keys(); iter.hasNext();) {
            String key = iter.next();
            sensorConfiguration.addSetting(key, sensorConfig.getString(key));
          }

          sensor = new CC2650(this.properties, name, bdAddress, addressType, sensorType, sensorConfiguration);
          break;
        default:
          break;
      }
      sensorList.add(sensor);
    }
    return sensorList;
  }

}
