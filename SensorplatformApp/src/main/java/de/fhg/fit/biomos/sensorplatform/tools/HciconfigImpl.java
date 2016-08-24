package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Pyka
 *
 */
public class HciconfigImpl implements Hciconfig {

  private static final Logger LOG = LoggerFactory.getLogger(HciconfigImpl.class);

  private static final String HCICONFIG = "hciconfig";
  private static final String HCI0 = "hci0";
  private static final String UP = "up";
  private static final String DOWN = "down";

  private static final Pattern BD_ADDRESS = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");

  private String localBDaddress;

  public HciconfigImpl() {
  }

  @Override
  public String getLocalBDaddress() {
    try {
      Process process = null;
      process = Runtime.getRuntime().exec(HCICONFIG);
      BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = null;
      while ((line = output.readLine()) != null) {
        Matcher m = BD_ADDRESS.matcher(line);
        if (m.find()) {
          LOG.info("local controller address: " + m.group(1));
          this.localBDaddress = m.group(1);
        }
      }
      process.waitFor();
      output.close();
    } catch (IOException | InterruptedException e) {
      LOG.error("getting local controller address failed", e);
    }
    return this.localBDaddress;
  }

  @Override
  public void up() {
    try {
      Runtime.getRuntime().exec(HCICONFIG + " " + HCI0 + " " + UP).waitFor();
      LOG.info("turn on hci0");
    } catch (IOException | InterruptedException e) {
      LOG.error("turn on hci0 failed", e);
    }
  }

  @Override
  public void down() {
    try {
      Runtime.getRuntime().exec(HCICONFIG + " " + HCI0 + " " + DOWN).waitFor();
      LOG.info("turn off hci0");
    } catch (IOException | InterruptedException e) {
      LOG.error("turn off hci0 failed", e);
    }
  }

}
