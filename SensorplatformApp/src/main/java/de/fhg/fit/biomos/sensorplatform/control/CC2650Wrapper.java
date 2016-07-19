package de.fhg.fit.biomos.sensorplatform.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.persistence.SampleLogger;
import de.fhg.fit.biomos.sensorplatform.sensor.CC2650;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

public class CC2650Wrapper implements SensorWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(CC2650Wrapper.class);

  private final CC2650 cc2650;
  private final Gatttool gatttool;

  private SampleLogger sampleLogger;

  private final Uploader uploader;

  public CC2650Wrapper(CC2650 cc2650, Uploader uploader) {
    this.cc2650 = cc2650;
    this.uploader = uploader;

    this.gatttool = new GatttoolImpl(this.cc2650.getAddressType(), cc2650.getBdaddress());
    this.gatttool.addObs(this);
    new Thread(this.gatttool).start();
  }

  @Override
  public void connectToSensor(int timeout) {
    // TODO Auto-generated method stub

  }

  @Override
  public void enableLogging() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disableLogging() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disconnectBlocking() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disconnect() {
    // TODO Auto-generated method stub

  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public void newNotificationData(ObservableSensorNotificationData observable, String handle, String rawHexValues) {
    // TODO Auto-generated method stub

  }

}
