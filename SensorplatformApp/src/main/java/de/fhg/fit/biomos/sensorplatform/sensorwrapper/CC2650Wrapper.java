package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.CC2650SampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Wrapper extends AbstractSensorWrapper<CC2650> {

  // private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650SampleCollector cc2650Collector;

  public CC2650Wrapper(CC2650 cc2650, String timeStampFormat, CC2650SampleCollector cc2650Collector) {
    super(cc2650, timeStampFormat);
    this.cc2650Collector = cc2650Collector;
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    this.lastNotificationTimestamp = System.currentTimeMillis();

    String data = rawHexValues.replace(" ", "");
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        // LOG.info("new temperature notification received");
        this.cc2650Collector.addToQueue(this.sensor.calculateTemperatureData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        // LOG.info("new humidity notification received");
        this.cc2650Collector.addToQueue(this.sensor.calculateHumidityData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        // LOG.info("new ambientlight notification received");
        this.cc2650Collector.addToQueue(this.sensor.calculateAmbientlightData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        // LOG.info("new pressure notification received");
        this.cc2650Collector.addToQueue(this.sensor.calculatePressureData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        // LOG.info("new movement notification received");
        this.cc2650Collector.addToQueue(this.sensor.calculateMovementSample(this.dtf.print(new DateTime()), data));
        break;
      default:
        // LOG.error("unexpected handle notification " + handle + " : " + rawHexValues);
        break;
    }
  }

}
