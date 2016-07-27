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

/**
 *
 * @author Daniel Pyka
 *
 */
public class HcitoolImpl implements Hcitool {

  private static final Logger LOG = LoggerFactory.getLogger(HcitoolImpl.class);

  private static final String SCAN_SCRIPT = "/bin/sh lescan.sh";
  private static final Pattern SENSOR_FOUND = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}) (.*)");

  private Process hcitool;
  private BufferedReader streamFromHcitool = null;

  public HcitoolImpl() {
    //
  }

  @Override
  public void collectLocalDeviceAndAddress() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<String> scan() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void lescan(int scanDuration) {
    //
  }

  @Override
  public List<String> findDevices(int scanDuration) {
    List<String> bdAddresses = new ArrayList<String>();
    try {
      this.hcitool = Runtime.getRuntime().exec(SCAN_SCRIPT + " " + scanDuration);
      this.streamFromHcitool = new BufferedReader(new InputStreamReader(this.hcitool.getInputStream()));
      LOG.info("hcitool started - LE scanning for " + scanDuration + "s ...");
      String line = null;
      while ((line = this.streamFromHcitool.readLine()) != null) {
        // System.out.println(line); // extreme debugging
        Matcher m = SENSOR_FOUND.matcher(line);
        if (m.find()) {
          if (!m.group(2).equals("(unknown)")) {
            LOG.info("RESULT mac: " + m.group(1) + " name: " + m.group(2));
            bdAddresses.add(m.group(1));
          }
        }
      }
      this.hcitool.waitFor();
      LOG.info("hcitool finished");
    } catch (IOException | InterruptedException e) {
      LOG.error("hcitool crashed");
      this.hcitool.destroy();
      e.printStackTrace();
    }
    return bdAddresses;
  }

}
