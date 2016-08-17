package de.fhg.fit.biomos.sensorplatform.tools;

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

public class BluetoothctlImpl implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(BluetoothctlImpl.class);

  private static final String BTCTL = "bluetoothctl";
  private static final String BTCTL_POWER_ON = "power on";
  private static final String BTCTL_POWER_OFF = "power off";
  private static final String BTCTL_SCAN_ON = "scan on";
  private static final String BTCTL_SCAN_OFF = "scan off";
  private static final String BTCTL_CONNECT = "connect";
  private static final String BTCTL_DISCONNECT = "disconnect";
  private static final String BTCTL_PAIR = "pair";
  private static final String BTCTL_REMOVE = "remove";
  private static final String BTCTL_EXIT = "exit";

  private static final Pattern PATTERN_LOCAL_BDADDRESS = Pattern.compile("Controller (\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");
  private static final Pattern PATTERN_DEVICE = Pattern.compile("\\[(\\w+)\\] Device (\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");

  private final List<String> foundDevices = new ArrayList<String>();

  private BufferedWriter streamToBluetoothctl = null;
  private BufferedReader streamFromBluetoothctl = null;

  public BluetoothctlImpl() {
    //
  }

  public String init() {
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
          return m.group(1);
        }
      }
    } catch (IOException e) {
      LOG.error("creating bluetoothctl process failed", e);
    }
    return "unknown";
  }

  @Override
  public void run() {
    try {
      String line = null;
      while ((line = BluetoothctlImpl.this.streamFromBluetoothctl.readLine()) != null) {
        Matcher m = PATTERN_DEVICE.matcher(line);
        if (m.find()) {
          if (m.group(1).equals("NEW")) {
            this.foundDevices.add(m.group(2));
            LOG.info("device " + m.group(2) + " found");
          } else if (m.group(1).equals("DEL")) {
            this.foundDevices.remove(m.group(2));
            LOG.info("device " + m.group(2) + " removed");
          }
        }
      }
    } catch (IOException e) {
      LOG.error("bluetoothctl crashed", e);
    }
  }

  public String getLocalControllerAddress() {
    String localController = init();
    exit();
    return localController;
  }

  public List<String> getFoundDevices() {
    return this.foundDevices;
  }

  public void powerOn() {
    try {
      this.streamToBluetoothctl.write(BTCTL_POWER_ON);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("controller powered on");
    } catch (IOException e) {
      LOG.error("failed to power on controller", e);
    }
  }

  public void powerOff() {
    try {
      this.streamToBluetoothctl.write(BTCTL_POWER_OFF);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("controller powered off");
    } catch (IOException e) {
      LOG.error("failed to power off controller", e);
    }
  }

  public void scanOn(String bdAddressToFind) {
    try {
      this.streamToBluetoothctl.write(BTCTL_SCAN_ON);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to turn on scan", e);
    }
  }

  public void scanOff() {
    try {
      this.streamToBluetoothctl.write(BTCTL_SCAN_OFF);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to turn off scan", e);
    }
  }

  public void connect(String bdAddress) {
    try {
      this.streamToBluetoothctl.write(BTCTL_CONNECT + " " + bdAddress);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
      LOG.info("connected to device " + bdAddress);
    } catch (IOException e) {
      LOG.error("failed to connect", e);
    }
  }

  public void disconnect() {
    try {
      this.streamToBluetoothctl.write(BTCTL_DISCONNECT);
      this.streamToBluetoothctl.newLine();
      this.streamToBluetoothctl.flush();
    } catch (IOException e) {
      LOG.error("failed to disconnect", e);
    }
  }

  public void pair() {
    try {
      this.streamToBluetoothctl.write(BTCTL_PAIR);
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
