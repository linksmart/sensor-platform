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
public class GatttoolImpl implements Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(GatttoolImpl.class);

  protected static final String GATTTTOOL_INTERACTIVE = "gatttool -I -t ";
  protected static final String CMD_EXIT = "exit";
  protected static final String CMD_CONNECT = "connect";
  protected static final String CMD_DISCONNECT = "disconnect";

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

  protected State state;

  protected AbstractSensorWrapper<?> observer;

  protected final String bdAddress;
  protected final AddressType addressType;
  protected final SecurityLevel secLevel;

  protected BufferedWriter streamToSensor = null;
  protected BufferedReader streamFromSensor = null;

  public GatttoolImpl(String bdAddress, AddressType addressType, SecurityLevel secLevel) {
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
   * Continuously read from the input stream of the gatttool process. React on specific patterns in the text (e.g. notifications).
   */
  @Override
  public void run() {
    try {
      String line = null;
      while ((line = this.streamFromSensor.readLine()) != null) {
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

  @Override
  public Gatttool.State getInternalState() {
    return this.state;
  }

  @Override
  public BufferedWriter getStreamToSensor() {
    return this.streamToSensor;
  }

  @Override
  public void setObserver(AbstractSensorWrapper<?> observer) {
    this.observer = observer;
  }

  @Override
  public void notifyObserver(String handle, String rawHexValues) {
    this.observer.newNotificationData(this, handle, rawHexValues);
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
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

}
