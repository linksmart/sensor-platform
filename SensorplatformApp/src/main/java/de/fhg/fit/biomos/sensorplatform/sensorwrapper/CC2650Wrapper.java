package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import org.joda.time.DateTime;

import de.fhg.fit.biomos.sensorplatform.control.CC2650SampleCollector;
import de.fhg.fit.biomos.sensorplatform.gatt.CC2650lib;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Wrapper extends AbstractSensorWrapper {

  // private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650 cc2650;
  private final CC2650SampleCollector cc2650Collector;

  public CC2650Wrapper(CC2650 cc2650, String timeStampFormat, CC2650SampleCollector cc2650Collector) {
    super(cc2650.getAddressType(), cc2650.getBDaddress(), timeStampFormat);
    this.cc2650 = cc2650;
    this.cc2650Collector = cc2650Collector;
  }

  @Override
  public void enableLogging() {
    this.cc2650.enableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
    this.lastNotificationTimestamp = System.currentTimeMillis();
  }

  @Override
  public void disableLogging() {
    this.cc2650.disableAllNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    this.lastNotificationTimestamp = System.currentTimeMillis();

    String data = rawHexValues.replace(" ", "");
    switch (handle) {
      case CC2650lib.HANDLE_IR_TEMPERATURE_VALUE:
        // LOG.info("new temperature notification received");
        this.cc2650Collector.addToQueue(this.cc2650.calculateTemperatureData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_HUMIDITY_VALUE:
        // LOG.info("new humidity notification received");
        this.cc2650Collector.addToQueue(this.cc2650.calculateHumidityData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_AMBIENTLIGHT_VALUE:
        // LOG.info("new ambientlight notification received");
        this.cc2650Collector.addToQueue(this.cc2650.calculateAmbientlightData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_PRESSURE_VALUE:
        // LOG.info("new pressure notification received");
        this.cc2650Collector.addToQueue(this.cc2650.calculatePressureData(this.dtf.print(new DateTime()), data));
        break;
      case CC2650lib.HANDLE_MOVEMENT_VALUE:
        // LOG.info("new movement notification received");
        this.cc2650Collector.addToQueue(this.cc2650.calculateMovementSample(this.dtf.print(new DateTime()), data));
        break;
      default:
        // LOG.error("unexpected handle notification " + handle + " : " + rawHexValues);
        break;
    }
  }

  @Override
  public void shutdown() {
    this.gatttool.exitGatttool();
  }

  @Override
  public String getBDaddress() {
    return this.cc2650.getBDaddress();
  }

  @Override
  public SensorName getDeviceName() {
    return this.cc2650.getName();
  }

  @Override
  public String toString() {
    return this.cc2650.getBDaddress() + " " + this.cc2650.getName();
  }

}
