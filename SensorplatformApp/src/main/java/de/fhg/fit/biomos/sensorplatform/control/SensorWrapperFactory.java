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
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;
import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * Factory for creating sensor objects. It is recommended to NOT create sensor objects directly but only through the factory.
 *
 * @author Daniel Pyka
 *
 */
public class SensorWrapperFactory {

  private static final Logger LOG = LoggerFactory.getLogger(SensorWrapperFactory.class);

  public static final String NAME = "name";
  public static final String BDADDRESS = "bdaddress";
  public static final String ADDRESSTYPE = "addresstype";
  public static final String WEBINTERFACE = "webinterface";
  public static final String SETTINGS = "settings";

  private final Properties properties;
  private final JSONArray sensorsConfiguration;

  public SensorWrapperFactory(Properties properties) {
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
  public List<SensorWrapper> createSensorsFromConfigurationFile() {
    List<SensorWrapper> sensorWrapperList = new ArrayList<SensorWrapper>();
    for (int i = 0; i < this.sensorsConfiguration.length(); i++) {

      JSONObject sensorDescription = this.sensorsConfiguration.getJSONObject(i);
      SensorName name = SensorName.valueOf(sensorDescription.getString(NAME));
      String bdAddress = sensorDescription.getString(BDADDRESS);
      AddressType addressType = AddressType.valueOf(sensorDescription.getString(ADDRESSTYPE));
      String webinterface = sensorDescription.getString(WEBINTERFACE);

      JSONObject settings = sensorDescription.getJSONObject(SETTINGS);

      String timestampFormat = this.properties.getProperty("logfile.timestamp.format");

      Uploader uploader = null;
      SensorWrapper sensorWrapper = null;

      // TODO not very flexible if we add other webinterfaces
      switch (webinterface) {
        case "telipro":
          uploader = new TeLiProUploader(this.properties);
          uploader.login();
          break;
        case "":
          LOG.info("No webinterface specified");
          break;
        default:
          LOG.error("Unknown webinterface name: " + webinterface);
          break;
      }

      switch (name) {
        case PolarH7:
          sensorWrapper = new PolarH7Wrapper(new PolarH7(name, bdAddress, addressType, timestampFormat, settings), uploader);
          break;
        case AdidasHRM:
          sensorWrapper = new AdidasHrmWrapper(new AdidasMiCoachHRM(name, bdAddress, addressType, timestampFormat, settings), uploader);
          break;
        case TomTomHRM:
          sensorWrapper = new TomTomHrmWrapper(new TomTomHRM(name, bdAddress, addressType, timestampFormat, settings), uploader);
          break;
        case PolarV800:
          break;
        case CC2650:
          // TODO
          sensorWrapper = new CC2650Wrapper(new CC2650(name, bdAddress, addressType, timestampFormat, settings), uploader);
          break;
        default:
          LOG.error("unknown sensor name " + name);
          break;
      }

      if (sensorWrapper != null) {
        sensorWrapperList.add(sensorWrapper);
      }
    }
    return sensorWrapperList;
  }

}
