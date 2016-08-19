package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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

  private final String databaseTimeStampFormat;

  @Inject
  public SensorWrapperFactory(HeartRateSampleCollector hrsCollector, CC2650SampleCollector cc2650Collector,
      @Named("database.timestamp.format") String databaseTimeStampFormat) {
    this.hrsCollector = hrsCollector;
    this.cc2650Collector = cc2650Collector;
    this.databaseTimeStampFormat = databaseTimeStampFormat;
  }

  /**
   *
   * @return List&lt;Sensor&gt; List of sensors the sensorplatform will work with
   */
  public List<AbstractSensorWrapper> createSensorWrapper(JSONArray sensorConfiguration) {
    LOG.info("creating sensorwrappers");
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
          sensorWrapper = new PolarH7Wrapper(new PolarH7(name, bdAddress, addressType, settings), this.databaseTimeStampFormat, this.hrsCollector);
          break;
        case AdidasHRM:
          sensorWrapper = new AdidasHrmWrapper(new AdidasMiCoachHRM(name, bdAddress, addressType, settings), this.databaseTimeStampFormat, this.hrsCollector);
          break;
        case TomTomHRM:
          sensorWrapper = new TomTomHrmWrapper(new TomTomHRM(name, bdAddress, addressType, settings), this.databaseTimeStampFormat, this.hrsCollector);
          break;
        case CC2650:
          sensorWrapper = new CC2650Wrapper(new CC2650(name, bdAddress, addressType, settings), this.databaseTimeStampFormat, this.cc2650Collector);
          break;
        case BLE113:
          // TODO
          break;
        default:
          LOG.error("unknown sensor name " + name);
          break;
      }

      if (sensorWrapper != null) {
        sensorWrapperList.add(sensorWrapper);
      }
    }
    LOG.info("finished creating sensorwrappers");
    return sensorWrapperList;
  }

}
