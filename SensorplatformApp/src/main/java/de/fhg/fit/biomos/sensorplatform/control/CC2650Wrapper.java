package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650Sample;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;

public class CC2650Wrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650 cc2650;
  private final Gatttool gatttool;

  private SampleLogger sampleLogger;

  public CC2650Wrapper(CC2650 cc2650) {
    this.cc2650 = cc2650;

    this.gatttool = new GatttoolImpl(this.cc2650.getAddressType(), cc2650.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();
  }

  @Override
  public void connectToSensor(int timeout) {
    try {
      this.gatttool.connect(timeout);
    } catch (BluetoothGattException e) {
      LOG.error(e.getMessage());
      this.gatttool.exitGatttool();
      LOG.info("Cannot connect to device, exit gatttool");
    }
  }

  @Override
  public void enableLogging() {
    this.sampleLogger = new SampleLogger(this.cc2650.getName().name());
    this.cc2650.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);

  }

  @Override
  public void disableLogging() {
    this.cc2650.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
    this.sampleLogger.close();
  }

  @Override
  public void disconnectBlocking() {
    this.gatttool.disconnectBlocking();
  }

  @Override
  public void disconnect() {
    this.gatttool.disconnect();
  }

  @Override
  public void shutdown() {
    this.gatttool.exitGatttool();
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    CC2650Sample sample = this.cc2650.calculateSensorData(handle, rawHexValues);

    this.sampleLogger.writeLine(sample.toString());

    // System.out.println(sample.toString()); // extreme debugging
  }

}
