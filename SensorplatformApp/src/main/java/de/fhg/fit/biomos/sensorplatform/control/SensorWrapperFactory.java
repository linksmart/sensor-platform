package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.sensor.AdidasMiCoachHRM;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AdidasHrmWrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.CC2650Wrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.PolarH7Wrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.TomTomHrmWrapper;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 * Factory for creating sensor objects. It is recommended to NOT create sensor objects directly but only through the factory.<br>
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class SensorWrapperFactory {

  private static final Logger LOG = LoggerFactory.getLogger(SensorWrapperFactory.class);

  public static final String NAME = "name";
  public static final String BDADDRESS = "bdaddress";
  public static final String ADDRESSTYPE = "addresstype";
  public static final String SETTINGS = "settings";

  private final HeartRateSampleCollector hrsCollector;
  private final CC2650SampleCollector cc2650Collector;

  private final String sensorConfigurationFileName;
  private final String logFileTimestampFormat;

  @Inject
  public SensorWrapperFactory(HeartRateSampleCollector hrsCollector, CC2650SampleCollector cc2650Collector,
      @Named("default.sensor.configuration.file") String sensorConfigurationFileName, @Named("logfile.timestamp.format") String logFileTimestampFormat) {
    this.hrsCollector = hrsCollector;
    this.cc2650Collector = cc2650Collector;
    this.sensorConfigurationFileName = sensorConfigurationFileName;
    this.logFileTimestampFormat = logFileTimestampFormat;
  }

  /**
   *
   * @return List&lt;Sensor&gt; List of sensors the sensorplatform will work with
   */
  public List<AbstractSensorWrapper> createSensorWrapperFromWebApp(JSONArray sensorConfiguration) {
    LOG.info("setup from webapplication configuration");
    return createSensorWrapper(sensorConfiguration);
  }

  /**
   * Creates sensors from a configuration file provided by the maven build process or an "external" file.
   *
   * @return List&lt;Sensor&gt; List of sensors the sensorplatform will work with
   */
  public List<AbstractSensorWrapper> createSensorWrapperFromProjectBuild() {
    LOG.info("setup from project build configuration");
    LOG.info("sensor configuration file " + this.sensorConfigurationFileName);

    JSONTokener tokener = new JSONTokener(ClassLoader.getSystemResourceAsStream(this.sensorConfigurationFileName));
    JSONArray sensorConfiguration = new JSONArray(tokener);

    return createSensorWrapper(sensorConfiguration);
  }

  private List<AbstractSensorWrapper> createSensorWrapper(JSONArray sensorConfiguration) {
    List<AbstractSensorWrapper> sensorWrapperList = new ArrayList<AbstractSensorWrapper>();
    for (int i = 0; i < sensorConfiguration.length(); i++) {

      JSONObject sensorConfigEntry = sensorConfiguration.getJSONObject(i);
      SensorName name = SensorName.valueOf(sensorConfigEntry.getString(NAME));
      String bdAddress = sensorConfigEntry.getString(BDADDRESS);
      AddressType addressType = AddressType.valueOf(sensorConfigEntry.getString(ADDRESSTYPE));
      JSONObject settings = sensorConfigEntry.getJSONObject(SETTINGS);

      AbstractSensorWrapper sensorWrapper = null;
      switch (name) {
        case PolarH7:
          sensorWrapper = new PolarH7Wrapper(new PolarH7(name, bdAddress, addressType, this.logFileTimestampFormat, settings), this.hrsCollector);
          break;
        case AdidasHRM:
          sensorWrapper = new AdidasHrmWrapper(new AdidasMiCoachHRM(name, bdAddress, addressType, this.logFileTimestampFormat, settings), this.hrsCollector);
          break;
        case TomTomHRM:
          sensorWrapper = new TomTomHrmWrapper(new TomTomHRM(name, bdAddress, addressType, this.logFileTimestampFormat, settings), this.hrsCollector);
          break;
        case PolarV800:
          LOG.error("PolarV800 not yet implemented - sensor will be ignored");
          break;
        case CC2650:
          sensorWrapper = new CC2650Wrapper(new CC2650(name, bdAddress, addressType, this.logFileTimestampFormat, settings), this.cc2650Collector);
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
