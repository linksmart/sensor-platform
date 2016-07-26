package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.TextFileLogger;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sensor.AdidasMiCoachHRM;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 *
 * @author Daniel Pyka
 *
 */
public class AdidasHrmWrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(AdidasHrmWrapper.class);

  private final AdidasMiCoachHRM adidasHrm;
  private final Uploader uploader;
  private final Gatttool gatttool;

  private final TextFileLogger sampleLogger;

  public AdidasHrmWrapper(AdidasMiCoachHRM adidasHrm, Uploader uploader) {
    this.adidasHrm = adidasHrm;
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(this.adidasHrm.getAddressType(), adidasHrm.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.sampleLogger = new TextFileLogger(this.adidasHrm.getName().name());
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
    this.adidasHrm.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
  }

  @Override
  public void disableLogging() {
    this.adidasHrm.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
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
    HeartRateSample hrs = this.adidasHrm.calculateHeartRateData(handle, rawHexValues);

    this.sampleLogger.writeLine(hrs.toString());

    // System.out.println(sample.toString()); // extreme debugging

    if (this.uploader != null) {
      this.uploader.addToQueue(hrs);
    }

  }

}
