package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

/**
 * Implementation of control class hcitool for Linux.
 *
 * @author Daniel Pyka
 *
 */
public class HcitoolImpl implements Hcitool {

  private static final Logger LOG = LoggerFactory.getLogger(HcitoolImpl.class);

  private static final Pattern NEWDEVICE = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}) (.*)");

  private final List<DetectedDevice> detectedDeviceList = new ArrayList<DetectedDevice>();

  public HcitoolImpl() {
  }

  @Override
  public List<DetectedDevice> scan(int duration) {
    this.detectedDeviceList.clear();
    try {
      LOG.info("scanning for {} seconds...", duration);
      Process process = Runtime.getRuntime().exec("timeout -s SIGINT " + duration + "s hcitool lescan");
      BufferedReader streamFromHcitool = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = streamFromHcitool.readLine()) != null) {
        Matcher m = NEWDEVICE.matcher(line);
        if (m.find()) {
          this.detectedDeviceList.add(new DetectedDevice(m.group(2), m.group(1)));
        }
      }
      process.waitFor();
      streamFromHcitool.close();
      LOG.info("scan finished");
    } catch (IOException | InterruptedException e) {
      LOG.error("scan failed", e);
    }
    return this.detectedDeviceList;
  }

}
