package de.fhg.fit.biomos.sensorplatform.tools;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

/**
 *
 * @author Daniel Pyka
 *
 */
public class HcitoolVirtual implements Hcitool {

  private static final Logger LOG = LoggerFactory.getLogger(HcitoolVirtual.class);

  @Override
  public List<DetectedDevice> scan(int duration) {
    LOG.info("performing a scan, providing mock data");
    List<DetectedDevice> detectedDeviceList = new ArrayList<DetectedDevice>();
    detectedDeviceList.add(new DetectedDevice("Polar H7", "11:22:33:44:55:66"));
    try {
      Thread.sleep(duration * 1000);
    } catch (InterruptedException e) {
      LOG.error("sleep failed", e);
    }
    return detectedDeviceList;
  }
}
