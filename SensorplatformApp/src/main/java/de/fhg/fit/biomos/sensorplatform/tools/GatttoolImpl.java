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

import de.fhg.fit.biomos.sensorplatform.control.ObservableSensorNotificationData;
import de.fhg.fit.biomos.sensorplatform.control.SensorNotificationDataObserver;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothGattException;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.tools.Gatttool}
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolImpl extends ObservableSensorNotificationData implements Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(GatttoolImpl.class);

  private static final String GATTTTOOL_INTERACTIVE = "gatttool -I -t ";
  private static final String CMD_EXIT = "exit";
  private static final String CMD_CONNECT = "connect";
  private static final String CMD_DISCONNECT = "disconnect";
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

  private static final Pattern NOTIFICATION_DATA = Pattern.compile("Notification handle = (\\dx\\d{4}) value: (.+)$");

  private enum STATE {
    CONNECTED, DISCONNECTED
  };

  private STATE state = STATE.DISCONNECTED;

  private BufferedWriter streamToSensor = null;
  private BufferedReader streamFromSensor = null;

  private final AddressType addressType;
  private final String bdAddress;

  public GatttoolImpl(AddressType addressType, String bdAddress) {
    this.addressType = addressType;
    this.bdAddress = bdAddress;
    this.state = STATE.DISCONNECTED;

    try {
      Process process = null;
      process = Runtime.getRuntime().exec(GATTTTOOL_INTERACTIVE + this.addressType.toString() + " -b " + bdAddress);
      this.streamToSensor = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      this.streamFromSensor = new BufferedReader(new InputStreamReader(process.getInputStream()));
      LOG.info("gatttool process for " + bdAddress + " created");
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }

  @Override
  public BufferedWriter getStreamToSensor() {
    return this.streamToSensor;
  }

  @Override
  public void addObs(SensorNotificationDataObserver sndo) {
    setObserver(sndo);
  }

  @Override
  public void removeObs() {
    deleteObserver();
  }

  @Override
  public void run() {
    try {
      String line = null;
      while ((line = GatttoolImpl.this.streamFromSensor.readLine()) != null) {
        // System.out.println(line); // extreme debugging
        Matcher m = NOTIFICATION_DATA.matcher(line);
        if (m.find()) {
          notifyObserver(m.group(1), m.group(2));
        } else if (line.contains("successful")) {
          this.state = STATE.CONNECTED;
        } else if (line.contains("disconnect")) {
          this.state = STATE.DISCONNECTED;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect(int timeout) throws BluetoothGattException {
    try {
      this.streamToSensor.write(CMD_CONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("Attempting to connect to " + this.bdAddress + " for " + timeout + "s");

      long startTime = System.currentTimeMillis();
      while (false || (System.currentTimeMillis() - startTime) < timeout * 1000) {
        if (this.state == STATE.CONNECTED) {
          LOG.info("connected");
          return;
        } else {
          Thread.sleep(50); // wait for incoming messages in the other thread
        }
      }

      if (this.state == STATE.DISCONNECTED) {
        throw new BluetoothGattException("Cannot connect to bluetooth device " + this.bdAddress);
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnectBlocking() {
    try {
      this.streamToSensor.write(CMD_DISCONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      while (this.state == STATE.CONNECTED) {
        Thread.sleep(50);
      }
      LOG.info("disconnected from " + this.bdAddress);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    try {
      this.streamToSensor.write(CMD_DISCONNECT);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("disconnected from " + this.bdAddress);
    } catch (IOException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }
  }

}
