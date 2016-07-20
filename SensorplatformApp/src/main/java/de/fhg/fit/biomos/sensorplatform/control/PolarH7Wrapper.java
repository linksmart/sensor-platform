package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.biomos.sensorplatform.sensors.PolarH7;
import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7Wrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(PolarH7Wrapper.class);

  private final PolarH7 polarh7;
  private final Gatttool gatttool;

  private SampleLogger sampleLogger;

  private final Uploader uploader;
  private final Thread uploaderThread;

  public PolarH7Wrapper(PolarH7 polarh7, Uploader uploader) {
    this.polarh7 = polarh7;
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(this.polarh7.getAddressType(), polarh7.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();

    this.uploaderThread = new Thread(this.uploader);
    this.uploaderThread.start();
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
    this.sampleLogger = new SampleLogger(this.polarh7.getName().name());
    this.polarh7.enableNotification(this.gatttool.getStreamToSensor(), GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.ENABLE_NOTIFICATION);
  }

  @Override
  public void disableLogging() {
    this.polarh7.disableNotification(GatttoolImpl.CMD_CHAR_WRITE_CMD, GatttoolImpl.DISABLE_NOTIFICATION);
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
    this.uploaderThread.interrupt();
  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    HeartRateSample hrs = this.polarh7.calculateHeartRateData(handle, rawHexValues);

    this.sampleLogger.writeLine(hrs.toString());

    // System.out.println(sample.toString()); // extreme debugging

    if (this.uploader != null) {
      this.uploader.addToQueue(hrs);
    }

  }

}
