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

import de.fhg.fit.biomos.sensorplatform.sensors.Sensor;

/**
 * @see {@link de.fhg.fit.biomos.sensorplatform.tools.Gatttool}
 *
 * @author Daniel Pyka
 *
 */
public class GatttoolImpl implements Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(GatttoolImpl.class);

  private static final String GATTTTOOL_INTERACTIVE_PUBLIC = "gatttool -I -t public -b ";
  private static final String GATTTTOOL_INTERACTIVE_RANDOM = "gatttool -I -t random -b ";
  private static final String CMD_EXIT = "exit";
  private static final String CMD_QUIT = "quit";
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

  public static final String ENABLE_MEASUREMENT = "01";
  public static final String DISABLE_MEASUREMENT = "00";
  public static final String ENABLE_NOTIFICATION = "01:00";
  public static final String DISABLE_NOTIFICATION = "00:00";

  private static final Pattern NOTIFICATION_DATA = Pattern.compile("Notification handle = (\\dx\\d{4}) value: (.+)$");

  private static final Runtime rt = Runtime.getRuntime();

  private BufferedWriter bw = null;
  private BufferedReader br = null;

  private final Sensor sensor;

  public GatttoolImpl(Sensor sensor, boolean randomBDaddress) {
    this.sensor = sensor;

    try {
      Process process = null;
      if (randomBDaddress) {
        process = rt.exec(GatttoolImpl.GATTTTOOL_INTERACTIVE_RANDOM + this.sensor.getBdaddress());
      } else {
        process = rt.exec(GatttoolImpl.GATTTTOOL_INTERACTIVE_PUBLIC + this.sensor.getBdaddress());
      }
      this.bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      this.br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      LOG.info("gatttool process for " + this.sensor.getBdaddress() + " created");
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }

  @Override
  public void run() {
    try {
      String line = null;
      while ((line = GatttoolImpl.this.br.readLine()) != null) {
        // System.out.println(line); // extreme debugging
        Matcher m = NOTIFICATION_DATA.matcher(line);
        if (m.find()) {
          GatttoolImpl.this.sensor.processSensorData(m.group(1), m.group(2));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect() {
    try {
      LOG.info("connect");
      this.bw.write(CMD_CONNECT);
      this.bw.newLine();
      this.bw.flush();
      Thread.sleep(1000);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enableLogging() {
    this.sensor.hook(this.bw);
    LOG.info(this.sensor.getName() + " hooked");
    this.sensor.enableLogging();
  }

  @Override
  public void disableLogging() {
    this.sensor.disableLogging();
    this.sensor.unhook();
    LOG.info(this.sensor.getName() + " unhooked");
  }

  @Override
  public void disconnectAndExit() {
    try {
      this.bw.write(CMD_DISCONNECT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("disconnect from " + this.sensor.getName());
      Thread.sleep(1000);
      this.bw.write(CMD_EXIT);
      this.bw.newLine();
      this.bw.flush();
      LOG.info("exit gatttool");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
