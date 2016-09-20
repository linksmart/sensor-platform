package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.ObservableSensorNotificationData;
import de.fhg.fit.biomos.sensorplatform.sensorwrapper.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolCmd;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolImpl extends ObservableSensorNotificationData implements Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(GatttoolImpl.class);

  private final String bdAddress;
  private final AddressType addressType;
  private final SecurityLevel secLevel;

  private State state;

  private BufferedWriter streamToSensor = null;
  private BufferedReader streamFromSensor = null;

  public GatttoolImpl(String bdAddress, AddressType addressType, SecurityLevel secLevel) {
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.secLevel = secLevel;
    this.state = State.DISCONNECTED;
    try {
      Process process = Runtime.getRuntime().exec(GatttoolCmd.GATTTTOOL_INTERACTIVE + this.addressType.toString() + " -b " + this.bdAddress);
      this.streamToSensor = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      this.streamFromSensor = new BufferedReader(new InputStreamReader(process.getInputStream()));
      LOG.info("gatttool process for " + this.bdAddress + " created");
      LOG.info("address type is " + this.addressType);
      this.streamToSensor.write(GatttoolCmd.CMD_SEC_LEVEL + " " + this.secLevel);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("security level set to " + this.secLevel);
    } catch (IOException e) {
      LOG.error("creating gatttool process failed", e);
    }
  }

  @Override
  public State getInternalState() {
    return this.state;
  }

  @Override
  public BufferedWriter getStreamToSensor() {
    return this.streamToSensor;
  }

  @Override
  public void addObs(SensorNotificationDataObserver abstractSensorWrapper) {
    setObserver(abstractSensorWrapper);
  }

  @Override
  public void run() {
    try {
      String line = null;
      while ((line = GatttoolImpl.this.streamFromSensor.readLine()) != null) {
        // System.out.println("!!! " + line); // extreme debugging
        Matcher m = GatttoolCmd.NOTIFICATION_DATA.matcher(line);
        if (m.find()) {
          notifyObserver(m.group(1), m.group(2));
        } else if (line.contains("successful")) {
          this.state = State.CONNECTED;
          LOG.info("state is " + this.state.name());
        } else if (line.contains("refused")) {
          this.state = State.DISCONNECTED;
          LOG.info("state is " + this.state.name());
        }
      }
      LOG.warn("closing gatttool streams");
      this.streamToSensor.close();
      this.streamFromSensor.close();
    } catch (IOException e) {
      LOG.error("gatttool crashed while attempting to read process output", e);
    }
  }

  @Override
  public SecurityLevel getSecurityLevel() {
    return this.secLevel;
  }

  @Override
  public AddressType getAddressType() {
    return this.addressType;
  }

  @Override
  public boolean connectBlocking(int timeout) {
    try {
      this.streamToSensor.write(GatttoolCmd.CMD_CONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("attempting to connect to " + this.bdAddress + " for " + timeout + "s");

      long startTime = System.currentTimeMillis();
      while ((System.currentTimeMillis() - startTime) < timeout * 1000) {
        if (this.state == State.CONNECTED) {
          return true;
        } else {
          Thread.sleep(50); // wait for incoming messages in the other thread
        }
      }

      if (this.state == State.DISCONNECTED) {
        LOG.error("cannot connect to bluetooth device " + this.bdAddress);
      }

    } catch (IOException | InterruptedException e) {
      LOG.error("connect failed", e);
    }
    return false;
  }

  @Override
  public void reconnect() {
    try {
      this.streamToSensor.write(GatttoolCmd.CMD_CONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      this.state = State.RECONNECTING;
      LOG.info("Attempting to reconnect to sensor for ca. 40s (nonblocking)");
    } catch (IOException e) {
      LOG.error("reconnect failed", e);
    }
  }

  @Override
  public void disconnect() {
    try {
      this.streamToSensor.write(GatttoolCmd.CMD_DISCONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      this.state = State.DISCONNECTED;
      LOG.info("disconnected from " + this.bdAddress);
    } catch (IOException e) {
      LOG.error("disconnect failed", e);
    }
  }

  @Override
  public void exitGatttool() {
    try {
      this.streamToSensor.write(GatttoolCmd.CMD_EXIT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("exit gatttool");
    } catch (IOException e) {
      LOG.error("exit gatttool failed", e);
    }
  }

}
