package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Pyka
 *
 */
public class HcitoolImpl implements Hcitool {

  private static final Logger LOG = LoggerFactory.getLogger(HcitoolImpl.class);

  // private static final String LESCAN_SCRIPT = "/home/pi/Sensorplatform/lescan.sh";
  private static final String LESCAN = "timeout -s SIGINT 5s hcitool lescan";

  private static final String HCITOOL = "hcitool";
  private static final String CONNECT = "lecc";
  private static final String DISCONNECT = "ledc";

  private static final Pattern NEWDEVICE = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");
  private static final Pattern CONNECTION_SUCCESSFUL = Pattern.compile("Connection handle (\\d+)");

  private final List<String> foundDevices = new ArrayList<String>();

  private final String bdAddress;
  private String handle = null;

  public HcitoolImpl(String bdAddress) {
    this.bdAddress = bdAddress;
  }

  @Override
  public void connect() {
    try {
      Process process = Runtime.getRuntime().exec(HCITOOL + " " + CONNECT + " " + this.bdAddress);
      BufferedReader streamFromHcitool = new BufferedReader(new InputStreamReader(process.getInputStream()));
      try {
        String line = null;
        while ((line = streamFromHcitool.readLine()) != null) {
          Matcher m = CONNECTION_SUCCESSFUL.matcher(line);
          if (m.find()) {
            LOG.info("connection handle " + m.group(1));
            this.handle = m.group(1);
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
  }

  @Override
  public void pair() {
    connect();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep between pairing steps failed", e);
    }
    disconnect();
  }

  @Override
  public void disconnect() {
    if (this.handle != null) {
      try {
        Runtime.getRuntime().exec(HCITOOL + " " + DISCONNECT + " " + this.handle).waitFor();
        this.handle = null;
        LOG.info("disconnected from device");
      } catch (IOException | InterruptedException e) {
        LOG.error("disconnect failed", e);
      }
    }
  }

  @Override
  public void scanFor(String bdAddress) {
    try {
      LOG.info("scanning for 5 seconds...");
      Process process = Runtime.getRuntime().exec(LESCAN);
      BufferedReader streamFromHcitool = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = streamFromHcitool.readLine()) != null) {
        Matcher m = NEWDEVICE.matcher(line);
        if (m.find()) {
          this.foundDevices.add(this.bdAddress);
        }
      }
      process.waitFor();
      streamFromHcitool.close();
      LOG.info("scan finished");
    } catch (IOException | InterruptedException e) {
      LOG.error("scan failed", e);
    }
  }

  @Override
  public List<String> getFoundDevices() {
    return this.foundDevices;
  }

}
