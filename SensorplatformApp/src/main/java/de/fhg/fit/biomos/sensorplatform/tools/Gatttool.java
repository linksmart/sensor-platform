package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
public class Gatttool implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Gatttool.class);

  public static final String GATTTTOOL_INTERACTIVE = "gatttool -I -t ";
  public static final String CMD_EXIT = "exit";
  public static final String CMD_CONNECT = "connect";
  public static final String CMD_DISCONNECT = "disconnect";

  public static final String CMD_PRIMARY = "primary";
  public static final String CMD_INCLUDED = "included";
  public static final String CMD_CHARACTERISTICS = "characteristics";
  public static final String CMD_CHAR_DESC = "char-desc";
  public static final String CMD_CHAR_READ_HND = "char-read-hnd";
  public static final String CMD_CHAR_READ_UUID = "char-read-uuid";
  public static final String CMD_CHAR_WRITE_REQ = "char-write-req";
  public static final String CMD_CHAR_WRITE_CMD = "char-write-cmd";
  public static final String CMD_SEC_LEVEL = "sec-level";
  public static final String CMD_MTU = "mtu";

  public static final String ENABLE_NOTIFICATION = "01:00";
  public static final String DISABLE_NOTIFICATION = "00:00";

  protected static final Pattern NOTIFICATION_DATA = Pattern.compile("Notification handle = (\\dx\\d{4}) value: (.+)$");

  public enum State {
    CONNECTED, DISCONNECTED, RECONNECTING
  };

  private AbstractSensorWrapper<?> observer;

  protected final String bdAddress;
  protected final AddressType addressType;
  protected final SecurityLevel secLevel;

  protected State state;

  protected BufferedWriter streamToSensor = null;
  protected BufferedReader streamFromSensor = null;

  public Gatttool(String bdAddress, AddressType addressType, SecurityLevel secLevel) {
    this.bdAddress = bdAddress;
    this.addressType = addressType;
    this.secLevel = secLevel;
    this.state = State.DISCONNECTED;
    try {
      Process process = Runtime.getRuntime().exec(GATTTTOOL_INTERACTIVE + this.addressType.toString() + " -b " + this.bdAddress);
      this.streamToSensor = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      this.streamFromSensor = new BufferedReader(new InputStreamReader(process.getInputStream()));
      LOG.info("gatttool process for " + this.bdAddress + " created");
      LOG.info("address type is " + this.addressType);
      this.streamToSensor.write(CMD_SEC_LEVEL + " " + this.secLevel);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("security level set to " + this.secLevel);
    } catch (IOException e) {
      LOG.error("creating gatttool process failed", e);
    }
  }

  /**
   * Expose the internal state of the Gatttool. SensorOverseer makes use of it.
   *
   * @return State the state the gatttool is currently in
   */
  public State getInternalState() {
    return this.state;
  }

  /**
   * Expose process input stream to be used in a sensorobject.
   *
   * @return stream to the gatttool process
   */
  public BufferedWriter getStreamToSensor() {
    return this.streamToSensor;
  }

  /**
   * Continuously read from the input stream of the gatttool process. React on specific patterns in the text (e.g. notifications).
   */
  @Override
  public void run() {
    try {
      String line = null;
      while ((line = Gatttool.this.streamFromSensor.readLine()) != null) {
        // System.out.println("!!! " + line); // extreme debugging
        Matcher m = NOTIFICATION_DATA.matcher(line);
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

  /**
   * Try to connect to a sensor.
   *
   * @param timeout
   *          int number of seconds to try connecting
   * @return true if connected successfully, false otherwise
   */
  public boolean connectBlocking(int timeout) {
    try {
      this.streamToSensor.write(CMD_CONNECT);
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

  /**
   * Try to connect to a sensor again, if gatttool has lost the previous connection (maybe because sensor went offline).
   */
  public void reconnect() {
    try {
      this.streamToSensor.write(CMD_CONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      this.state = State.RECONNECTING;
      LOG.info("Attempting to reconnect to sensor for ca. 40s (nonblocking)");
    } catch (IOException e) {
      LOG.error("reconnect failed", e);
    }
  }

  /**
   * Disconnect gracefully from the sensor.
   */
  public void disconnect() {
    try {
      this.streamToSensor.write(CMD_DISCONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      this.state = State.DISCONNECTED;
      LOG.info("disconnected from " + this.bdAddress);
    } catch (IOException e) {
      LOG.error("disconnect failed", e);
    }
  }

  /**
   * Exit gatttool gracefully to properly shut down the process and release all resources.
   */
  public void exitGatttool() {
    try {
      this.streamToSensor.write(CMD_EXIT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("exit gatttool");
    } catch (IOException e) {
      LOG.error("exit gatttool failed", e);
    }
  }

  /**
   * Set the observer.
   *
   * @param observer
   *          SensorWrapper is the observer
   */
  public void setObserver(AbstractSensorWrapper<?> observer) {
    this.observer = observer;
  }

  /**
   * Notify the SensorWrapper that a new notification has arrived in Gatttool.
   *
   * @param handle
   *          handle address of the notification
   * @param rawHexValues
   *          hexadecimal values of the notification
   */
  public void notifyObserver(String handle, String rawHexValues) {
    this.observer.newNotificationData(this, handle, rawHexValues);
  }

}
