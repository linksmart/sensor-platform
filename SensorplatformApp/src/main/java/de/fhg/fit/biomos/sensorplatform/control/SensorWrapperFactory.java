package de.fhg.fit.biomos.sensorplatform.control;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.sensor.AdidasHRM;
import de.fhg.fit.biomos.sensorplatform.sensor.BLE113;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.sensor.PolarH7;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.CC2650Wrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.HeartRateSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.PulseOximeterSensorWrapper;
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
  private final PulseOximeterSampleCollector pulseCollector;
  private final CC2650SampleCollector cc2650Collector;

  private final String databaseTimeStampFormat;

  @Inject
  public SensorWrapperFactory(HeartRateSampleCollector hrsCollector, CC2650SampleCollector cc2650Collector, PulseOximeterSampleCollector pulseCollector,
      @Named("database.timestamp.format") String databaseTimeStampFormat) {
    this.hrsCollector = hrsCollector;
    this.cc2650Collector = cc2650Collector;
    this.pulseCollector = pulseCollector;
    this.databaseTimeStampFormat = databaseTimeStampFormat;
  }

  /**
   * Gets a description as JSONArray. Creates corresponding objects from that. This is similiar to reflection. Sensorname Enum must be used.
   *
   * @param sensorConfiguration
   *          configuration from the web application
   * @param firstname
   *          the first name of the sensorplatform user
   * @param lastname
   *          the last name of the sensorplatform user
   * @return A list of all sensorwrappers to use during recording
   */
  public List<AbstractSensorWrapper<?>> createSensorWrapper(JSONArray sensorConfiguration, String firstname, String lastname) {
    LOG.info("creating sensorwrappers");
    List<AbstractSensorWrapper<?>> sensorWrapperList = new ArrayList<AbstractSensorWrapper<?>>();
    for (int i = 0; i < sensorConfiguration.length(); i++) {
      try {
        JSONObject sensorConfigEntry = sensorConfiguration.getJSONObject(i);
        SensorName name = SensorName.valueOf(sensorConfigEntry.getString(NAME));
        String bdAddress = sensorConfigEntry.getString(BDADDRESS);
        JSONObject settings = sensorConfigEntry.getJSONObject(SETTINGS);

        switch (name) {
          case PolarH7:
            HeartRateSensorWrapper polarh7Wrapper = new HeartRateSensorWrapper(new PolarH7(name, bdAddress, settings), this.databaseTimeStampFormat, firstname,
                lastname, this.hrsCollector);
            sensorWrapperList.add(polarh7Wrapper);
            break;
          case AdidasHRM:
            HeartRateSensorWrapper adidasHrmWrapper = new HeartRateSensorWrapper(new AdidasHRM(name, bdAddress, settings), this.databaseTimeStampFormat,
                firstname, lastname, this.hrsCollector);
            sensorWrapperList.add(adidasHrmWrapper);
            break;
          case TomTomHRM:
            HeartRateSensorWrapper tomtomHrmWrapper = new HeartRateSensorWrapper(new TomTomHRM(name, bdAddress, settings), this.databaseTimeStampFormat,
                firstname, lastname, this.hrsCollector);
            sensorWrapperList.add(tomtomHrmWrapper);
            break;
          case CC2650:
            CC2650Wrapper cc2650Wrapper = new CC2650Wrapper(new CC2650(name, bdAddress, settings), this.databaseTimeStampFormat, firstname, lastname,
                this.cc2650Collector);
            sensorWrapperList.add(cc2650Wrapper);
            break;
          case BLE113:
            PulseOximeterSensorWrapper ble113Wrapper = new PulseOximeterSensorWrapper(new BLE113(name, bdAddress, settings), this.databaseTimeStampFormat,
                firstname, lastname, this.pulseCollector);
            sensorWrapperList.add(ble113Wrapper);
            break;
          default:
            LOG.error("unknown sensor name {}", name);
            break;
        }
      } catch (Exception e) {
        LOG.error("bad json fields - skip this sensor", e);
      }
    }
    LOG.info("finished creating sensorwrappers");
    return sensorWrapperList;
  }

}
