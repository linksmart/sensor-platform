package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
public class BluetoothctlImpl implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(BluetoothctlImpl.class);

  private static final String BTCTL = "bluetoothctl";
  private static final String BTCTL_POWER_ON = "power on";
  private static final String BTCTL_POWER_OFF = "power off";
  private static final String BTCTL_AGENT = "agent";
  private static final String BTCTL_DEFAULTAGENT = "default-agent";
  private static final String BTCTL_SCAN_ON = "scan on";
  private static final String BTCTL_SCAN_OFF = "scan off";
  private static final String BTCTL_CONNECT = "connect";
  private static final String BTCTL_DISCONNECT = "disconnect";
  private static final String BTCTL_PAIR = "pair";
  private static final String BTCTL_REMOVE = "remove";
  private static final String BTCTL_EXIT = "exit";

  private static final Pattern PATTERN_LOCAL_BDADDRESS = Pattern.compile("Controller (\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");
  private static final Pattern PATTERN_DEVICE = Pattern.compile("\\[(\\w+)\\] Device (\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");
  private static final String CONNECTED = "Connection successful";
  private static final String CONNECT_ERROR = "Failed to connect";
  // private static final String DISCONNECTED = "Successful disconnected";

  private final List<String> detectedDevices = new ArrayList<String>();

  private BufferedWriter streamToBluetoothctl = null;
  private BufferedReader streamFromBluetoothctl = null;

  private String localController = null;

  public BluetoothctlImpl() {
    try {
      Process process = null;
      process = Runtime.getRuntime().exec(BTCTL);
      this.streamToBluetoothctl = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      this.streamFromBluetoothctl = new BufferedReader(new InputStreamReader(process.getInputStream()));
      LOG.info("bluetoothctl process created");
      String line = null;
      while ((line = BluetoothctlImpl.this.streamFromBluetoothctl.readLine()) != null) {
        Matcher m = PATTERN_LOCAL_BDADDRESS.matcher(line);
        if (m.find()) {
          LOG.info("local controller address: " + m.group(1));
          this.localController = m.group(1);
          break;
        }
      }
    } catch (IOException e) {
      LOG.error("creating bluetoothctl process failed", e);
    }
  }

  @Override
  public void run() {
    try {
      String line = null;
      while ((line = BluetoothctlImpl.this.streamFromBluetoothctl.readLine()) != null) {
        Matcher m = PATTERN_DEVICE.matcher(line);
        if (m.find()) {
          if (m.group(1).equals("NEW")) {
            this.detectedDevices.add(m.group(2));
            LOG.info("device " + m.group(2) + " found");
          } else if (m.group(1).equals("DEL")) {
            this.detectedDevices.remove(m.group(2));
            LOG.info("device " + m.group(2) + " removed");
          }
        }
      }
    } catch (IOException e) {
      LOG.error("bluetoothctl crashed", e);
    }
  }

  public String getLocalControllerAddress() {
    return this.localController;
  }

  public List<String> getFoundDevices() {
    return this.detectedDevices;
  }

  public void init(BluetoothAgent btAgent) {
    powerOff();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("power off/on sleep failed", e);
    }
    powerOn();
    setAgent(btAgent);
  }

  private void powerOn() {
    try {
      this.streamToBluetoothctl.write(BTCTL_POWER_ON);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("controller powered on");
    } catch (IOException e) {
      LOG.error("failed to power on controller", e);
    }
  }

  private void powerOff() {
    try {
      this.streamToBluetoothctl.write(BTCTL_POWER_OFF);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("controller powered off");
    } catch (IOException e) {
      LOG.error("failed to power off controller", e);
    }
  }

  private void setAgent(BluetoothAgent btAgent) {
    try {
      this.streamToBluetoothctl.write(BTCTL_AGENT + " " + btAgent);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      this.streamToBluetoothctl.write(BTCTL_DEFAULTAGENT);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("agent set to " + btAgent);
    } catch (IOException e) {
      LOG.error("failed to power off controller", e);
    }
  }

  public void scanFor(String bdAddressToFind) {
    try {
      this.streamToBluetoothctl.write(BTCTL_SCAN_ON);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      while (!this.detectedDevices.contains(bdAddressToFind)) {
        Thread.sleep(500);
      }
      LOG.info("device " + bdAddressToFind + " found");
      scanOff();
    } catch (IOException | InterruptedException e) {
      LOG.error("scan for bdaddress failed", e);
    }
  }

  private void scanOff() {
    try {
      this.streamToBluetoothctl.write(BTCTL_SCAN_OFF);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("turn off scan");
    } catch (IOException e) {
      LOG.error("failed to turn off scan", e);
    }
  }

  public boolean connect(String bdAddress) {
    try {
      this.streamToBluetoothctl.write(BTCTL_CONNECT + " " + bdAddress);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      String line;
      while ((line = BluetoothctlImpl.this.streamFromBluetoothctl.readLine()) != null) {
        if (line.contains(CONNECTED)) {
          LOG.info("connected to device " + bdAddress);
          return true;
        } else if (line.contains(CONNECT_ERROR)) {
          LOG.error("cannot connect to device" + bdAddress);
          return false;
        }
      }
    } catch (IOException e) {
      LOG.error("failed to connect", e);
    }
    return false;
  }

  public void disconnect(String bdAddress) {
    try {
      this.streamToBluetoothctl.write(BTCTL_DISCONNECT + " " + bdAddress);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to disconnect", e);
    }
  }

  public void pair(String bdAddress) {
    try {
      this.streamToBluetoothctl.write(BTCTL_PAIR + " " + bdAddress);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to pair", e);
    }
  }

  public void remove(String bdAddress) {
    try {
      this.streamToBluetoothctl.write(BTCTL_REMOVE + " " + bdAddress);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to remove device", e);
    }
  }

  public void exit() {
    try {
      this.streamToBluetoothctl.write(BTCTL_EXIT);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to exit bluetoothctl", e);
    }
  }

}
