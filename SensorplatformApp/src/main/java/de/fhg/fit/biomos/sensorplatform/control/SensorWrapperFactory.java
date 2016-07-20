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

  public SensorWrapperFactory() {
    // unused
  }

  /**
   * TODO Placeholder for configuration through webapplication
   *
   * @return
   */
  public static List<SensorWrapper> setupFromWebinterfaceConfinguration() {
    return null;
  }

  /**
   * Creates sensors from a configuration file provided by the maven build process or an "external" file.
   *
   * @return List&lt;Sensor&gt; List of sensors the sensorplatform shall work with
   */
  public static List<SensorWrapper> setupFromProjectBuildConfiguration(Properties properties) {
    LOG.info("setup from project build configuration");
    String sensorsConfigurationFilename = properties.getProperty("default.sensor.configuration.file");
    LOG.info("sensor configuration file " + sensorsConfigurationFilename);

    JSONTokener tokener = new JSONTokener(ClassLoader.getSystemResourceAsStream(sensorsConfigurationFilename));
    JSONArray sensorConfiguration = new JSONArray(tokener);

    List<SensorWrapper> sensorWrapperList = new ArrayList<SensorWrapper>();
    for (int i = 0; i < sensorConfiguration.length(); i++) {

      JSONObject sensorConfigEntry = sensorConfiguration.getJSONObject(i);
      SensorName name = SensorName.valueOf(sensorConfigEntry.getString(NAME));
      String bdAddress = sensorConfigEntry.getString(BDADDRESS);
      AddressType addressType = AddressType.valueOf(sensorConfigEntry.getString(ADDRESSTYPE));
      String timestampFormat = properties.getProperty("logfile.timestamp.format");
      JSONObject settings = sensorConfigEntry.getJSONObject(SETTINGS);

      // FIXME not very flexible if we would add other webinterfaces
      Uploader uploader = sensorConfigEntry.getString(WEBINTERFACE).equals("telipro") ? new TeLiProUploader(properties) : null;

      SensorWrapper sensorWrapper = null;
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
          LOG.error("PolarV800 not yet implemented - sensor will be ignored");
          break;
        case CC2650:
          // no uploader, upload to any webinterface not intended for CC2650
          sensorWrapper = new CC2650Wrapper(new CC2650(name, bdAddress, addressType, timestampFormat, settings));
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
