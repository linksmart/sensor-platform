package de.fhg.fit.biomos.sensorplatform.system;

import java.net.NetworkInterface;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigMock;

/**
 * For a local installation on a windows machine..
 *
 * @author Daniel Pyka
 *
 */
public class Windows implements HardwarePlatform {

  private static final Logger LOG = LoggerFactory.getLogger(Windows.class);

  private static final String INTERNET_INTERFACE_NAME = "eth3";

  private final Hciconfig hciconfig;

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
    this.hciconfig = new HciconfigMock();
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
  public void printInternetInterfaceInfo() throws SocketException, NullPointerException {
    NetworkInterface inet = NetworkInterface.getByName(INTERNET_INTERFACE_NAME);
    LOG.info(inet.getDisplayName() + " " + inet.getInetAddresses().nextElement().getHostAddress() + " " + inet.isUp());
  }

  @Override
  public void connectToMobileInternet() {
    LOG.info("do nothing");
  }

}
