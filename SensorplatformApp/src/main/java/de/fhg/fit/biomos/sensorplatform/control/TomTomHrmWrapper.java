package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.TomTomHRM;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;
import de.fhg.fit.biomos.sensorplatform.util.Unit;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHrmWrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TomTomHrmWrapper.class);

  private final TomTomHRM tomtomhrm;
  private final Gatttool gatttool;

  private SampleLogger sampleLogger;

  private final Uploader uploader;

  public TomTomHrmWrapper(TomTomHRM tomtomhrm, Uploader uploader) {
    this.tomtomhrm = tomtomhrm;
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(this.tomtomhrm.getAddressType(), tomtomhrm.getBdaddress());
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
    this.sampleLogger = new SampleLogger("hrm", this.tomtomhrm.getName().name());
    this.sampleLogger.addDescriptionLine("Heartrate [" + Unit.BPM + "]");
    this.tomtomhrm.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
  }

  @Override
  public void disableLogging() {
    this.tomtomhrm.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
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
    System.out.println("obs: handle " + handle + " " + "rawHexData " + rawHexValues);
    HeartRateSample sample = this.tomtomhrm.calculateHeartRateData(handle, rawHexValues);
    if (sample != null) {
      this.sampleLogger.writeLine(sample.toString());
      this.sampleLogger.writeLine(sample.toString());

      System.out.println(sample.toString());

      if (this.uploader != null) {
        this.uploader.sendData(this.tomtomhrm.getBdaddress(), sample.getHeartRate());
      }
    } else {
      LOG.error("heart rate sample is null");
    }
  }

}
