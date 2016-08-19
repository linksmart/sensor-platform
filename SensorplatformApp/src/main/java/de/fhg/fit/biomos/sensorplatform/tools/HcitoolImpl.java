package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

  private static final String HCITOOL = "hcitool";
  private static final String CONNECT = "lecc";
  private static final String DISCONNECT = "ledc";

  private static final Pattern CONNECTION_SUCCESSFUL = Pattern.compile("Connection handle (\\d+)");

  @Override
  public String connect(String bdAddress) {
    try {
      Process process = Runtime.getRuntime().exec(HCITOOL + " " + CONNECT + " " + bdAddress);
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      // TODO
      process.waitFor();
      LOG.info("connected to device");
    } catch (IOException | InterruptedException e) {
      LOG.error("connect failed", e);
    }
    return null;
  }

  @Override
  public void pair(String bdAddress) {
    disconnect(connect(bdAddress));
  }

  @Override
  public void disconnect(String handle) {
    try {
      Runtime.getRuntime().exec(HCITOOL + " " + DISCONNECT + " " + handle).waitFor();
      LOG.info("disconnected from device");
    } catch (IOException | InterruptedException e) {
      LOG.error("disconnect failed", e);
    }
  }

  @Override
  public void scan() {

  }

  @Override
  public String getFoundDevices() {
    return null;
  }

}
