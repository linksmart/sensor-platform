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
 *
 * @author Daniel Pyka
 *
 */
public class HcitoolImpl implements Hcitool {

  private static final Logger LOG = LoggerFactory.getLogger(HcitoolImpl.class);

  private static final String HCITOOL = "hcitool";
  private static final String CONNECT = "lecc";
  private static final String DISCONNECT = "ledc";

  private static final Pattern NEWDEVICE = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}) (.*)");
  private static final Pattern CONNECTION_SUCCESSFUL = Pattern.compile("Connection handle (\\d+)");

  private final int scanDuration;
  private final String LESCAN;

  private final List<DetectedDevice> detectedDeviceList = new ArrayList<DetectedDevice>();

  public HcitoolImpl(int scanDuration) {
    this.scanDuration = scanDuration;
    this.LESCAN = "timeout -s SIGINT " + this.scanDuration + "s hcitool lescan";
  }

  @Override
  public String connect(String bdAddress) {
    try {
      Process process = Runtime.getRuntime().exec(HCITOOL + " " + CONNECT + " " + bdAddress);
      BufferedReader streamFromHcitool = new BufferedReader(new InputStreamReader(process.getInputStream()));
      try {
        String line = null;
        while ((line = streamFromHcitool.readLine()) != null) {
          Matcher m = CONNECTION_SUCCESSFUL.matcher(line);
          if (m.find()) {
            LOG.info("connection handle " + m.group(1));
            return m.group(1);
          }
        }
      } catch (IOException e) {
        LOG.error("Cannot read from hcitool process", e);
      }
      process.waitFor();
      streamFromHcitool.close();
    } catch (IOException | InterruptedException e) {
      LOG.error("connect failed", e);
    }
    return "";
  }

  @Override
  public void pair(String bdAddress) {
    String handle = connect(bdAddress);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep between pairing steps failed", e);
    }
    disconnect(handle);
  }

  @Override
  public void disconnect(String handle) {
    if (handle != null) {
      try {
        Runtime.getRuntime().exec(HCITOOL + " " + DISCONNECT + " " + handle).waitFor();
        LOG.info("disconnected from device");
      } catch (IOException | InterruptedException e) {
        LOG.error("disconnect failed", e);
      }
    }
  }

  @Override
  public List<DetectedDevice> scan() {
    this.detectedDeviceList.clear();
    try {
      LOG.info("scanning for " + this.scanDuration + " seconds...");
      Process process = Runtime.getRuntime().exec(this.LESCAN);
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

  @Override
  public List<DetectedDevice> getDetectedDevices() {
    return this.detectedDeviceList;
  }

}
