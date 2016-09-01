package de.fhg.fit.biomos.sensorplatform.control;

import java.net.NetworkInterface;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;

/**
 * Handles everything related to mobile internet connection.
 *
 * @author Daniel Pyka
 *
 */
public class InternetConnectionManager implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(InternetConnectionManager.class);

  private final Long internetCheckInterval;
  private final HardwarePlatform hwPlatform;

  @Inject
  public InternetConnectionManager(@Named("internet.check.interval") String internetCheckInterval, HardwarePlatform hwPlatform) {
    this.internetCheckInterval = new Long(internetCheckInterval) * 1000;
    this.hwPlatform = hwPlatform;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        printPPP0Information();
      } catch (SocketException | NullPointerException e) {
        LOG.info("interface ppp0 not running - starting pppd now");
        this.hwPlatform.connectToMobileInternet();
      }
      try {
        Thread.sleep(this.internetCheckInterval);
      } catch (InterruptedException e) {
        LOG.error("sleep failed", e);
      }
    }
    // we should never reach this line during runtime
    LOG.info("thread finished");
  }

  public void printPPP0Information() throws SocketException, NullPointerException {
    NetworkInterface inet = NetworkInterface.getByName("ppp0");
    LOG.info(inet.getDisplayName() + " " + inet.getInetAddresses().nextElement().getHostAddress() + " " + inet.isUp());
  }

}
