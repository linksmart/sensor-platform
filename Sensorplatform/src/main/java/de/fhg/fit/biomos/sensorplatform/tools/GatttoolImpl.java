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

  public static final String[] BASH = { "/bin/bash", "-i" };
  public static final String GATTTTOOL_INTERACTIVE_PUBLIC = "gatttool -I -t public -b ";
  public static final String GATTTTOOL_INTERACTIVE_RANDOM = "gatttool -I -t random -b ";
  public static final String CMD_EXIT = "exit";
  public static final String CMD_QUIT = "quit";
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

  public static final String ENABLE_MEASUREMENT = "01";
  public static final String DISABLE_MEASUREMENT = "00";
  public static final String ENABLE_NOTIFICATION = "01:00";
  public static final String DISABLE_NOTIFICATION = "00:00";

  private static final Pattern NOTIFICATION_DATA = Pattern.compile("Notification handle = (\\dx\\d{4}) value: (.+)$");

  private Process process = null;
  private BufferedWriter bw = null;
  private BufferedReader br = null;
  private Thread input = null;

  private Sensor sensor;

  public GatttoolImpl(Sensor sensor) {
    this.sensor = sensor;
    try {
      this.process = Runtime.getRuntime().exec(BASH);
      this.bw = new BufferedWriter(new OutputStreamWriter(GatttoolImpl.this.process.getOutputStream()));
      this.br = new BufferedReader(new InputStreamReader(GatttoolImpl.this.process.getInputStream()));
      LOG.info("new bash process created");
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    this.input = new Thread() {
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
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    this.input.start();
  }

  @Override
  public void setup() {
    try {
      openGatttool();
      Thread.sleep(1000);
      connectGatttool();
      Thread.sleep(1000);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void openGatttool() throws IOException {
    LOG.info("create gatttool");
    LOG.info("target: " + this.sensor.getBdaddress());
    this.bw.write(GATTTTOOL_INTERACTIVE_PUBLIC + this.sensor.getBdaddress());
    this.bw.newLine();
    this.bw.flush();
  }

  private void connectGatttool() throws IOException, InterruptedException {
    LOG.info("connect");
    this.bw.write(CMD_CONNECT);
    this.bw.newLine();
    this.bw.flush();
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
    this.sensor.hook(null);
    LOG.info(this.sensor.getName() + " unhooked");
  }

  @Override
  public void closeGracefully() {
    try {
      disconnect();
      Thread.sleep(500);
      exitGatttool();
      Thread.sleep(500);
      exitBash();
      Thread.sleep(500);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void disconnect() throws IOException {
    this.bw.write(CMD_DISCONNECT);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("disconnect from sensor");
  }

  private void exitGatttool() throws IOException {
    this.bw.write(CMD_QUIT);
    this.bw.newLine();
    this.bw.flush();
    LOG.info("exit gatttool");
  }

  private void exitBash() throws IOException {
    this.bw.write(CMD_EXIT);
    this.bw.newLine();
    this.bw.flush();
    this.bw.close();
    LOG.info("exit bash");
  }
}
