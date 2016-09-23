package de.fhg.fit.biomos.sensorplatform.system;

import java.net.NetworkInterface;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigVirtual;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolVirtual;

/**
 * For a local installation on a windows machine. Only for testing parts of the application.
 *
 * @author Daniel Pyka
 *
 */
public class Windows implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(Windows.class);

  private static final String INTERNET_INTERFACE_NAME = "eth3";

  private final Hciconfig hciconfig;
  private final Hcitool hcitool;

  private enum LEDstate {
    STANDBY("timer"), RECORDING("heartbeat"), ERROR("none");

    private final String state;

    private LEDstate(String state) {
      this.state = state;
    }

    @Override
    public String toString() {
      return this.state;
    }

  }

  public Windows() {
    this.hciconfig = new HciconfigVirtual();
    this.hcitool = new HcitoolVirtual();
  }

  @Override
  public void setLEDstateSTANDBY() {
    LOG.info("LED " + LEDstate.STANDBY.name());
  }

  @Override
  public void setLEDstateRECORDING() {
    LOG.info("LED " + LEDstate.RECORDING.name());
  }

  @Override
  public void setLEDstateERROR() {
    LOG.info("LED " + LEDstate.ERROR.name());
  }

  @Override
  public Hciconfig getBluetoothController() {
    return this.hciconfig;
  }

  @Override
  public Hcitool getHcitool() {
    return this.hcitool;
  }

  @Override
  public boolean printInternetInterfaceInfo() throws SocketException, NullPointerException {
    NetworkInterface inet = NetworkInterface.getByName(INTERNET_INTERFACE_NAME);
    LOG.info(inet.getDisplayName() + " " + inet.getInetAddresses().nextElement().getHostAddress() + " " + inet.isUp());
    return inet.isUp();
  }

  @Override
  public void connectToMobileInternet() {
    LOG.info("do nothing");
  }

}
