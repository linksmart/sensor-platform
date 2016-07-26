package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.TextFileLogger;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHrmWrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomHrmWrapper.class);

  private final TomTomHRM tomtomhrm;
  private final Uploader uploader;
  private final Gatttool gatttool;

  private final TextFileLogger sampleLogger;

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, Uploader uploader) {
    this.tomtomhrm = tomtomhrm;
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(this.tomtomhrm.getAddressType(), tomtomhrm.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.sampleLogger = new TextFileLogger(this.tomtomhrm.getName().name());
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
    this.tomtomhrm.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
  }

  @Override
  public void disableLogging() {
    this.tomtomhrm.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
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
    this.sampleLogger.close();
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    HeartRateSample hrs = this.tomtomhrm.calculateHeartRateData(handle, rawHexValues, false);

    this.sampleLogger.writeLine(hrs.toString());

    // System.out.println(sample.toString()); // extreme debugging

    if (this.uploader != null) {
      this.uploader.addToQueue(hrs);
    }

  }

}
