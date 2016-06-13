package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.SensorDescription;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.matcher.Matchers;

public class GatttoolImpl implements Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(GatttoolImpl.class);

  private static final String SHELL = "/bin/sh";
  private static final String GATTTTOOL_INTERACTIVE = "sudo gatttool -I";
  private static final String CMD_EXIT = "exit";
  private static final String CMD_QUIT = "quit";
  private static final String CMD_CONNECT = "connect";
  private static final String CMD_DISCONNECT = "disconnect";
  private static final String CMD_PRIMARY = "primary";
  private static final String CMD_INCLUDED = "included";
  private static final String CMD_CHARACTERISTICS = "characteristics";
  private static final String CMD_CHAR_DESC = "char-desc";
  private static final String CMD_CHAR_READ_HND = "char-read-hnd";
  private static final String CMD_CHAR_READ_UUID = "char-read-uuid";
  private static final String CMD_CHAR_WRITE_REQ = "char-write-req";
  private static final String CMD_CHAR_WRITE_CMD = "char-write-cmd";
  private static final String CMD_SEC_LEVEL = "sec-level";
  private static final String CMD_MTU = "mtu";

  // private static final String CONN_SUCCESS = "Connection successful";

  private static final Pattern PATTERN_DISCONNECTED = Pattern.compile("\\[\\s+\\]\\[LE\\]\\p{Punct}");
  private static final Pattern PATTERN_CONNECTED = Pattern.compile("\\[\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}\\]\\[LE\\]\\p{Punct}");

  private final SensorDescription sd;
  private Process process;
  private Expect expect;

  public GatttoolImpl(SensorDescription sd) {
    this.sd = sd;

    try {
      this.process = Runtime.getRuntime().exec(SHELL);
      this.expect = new ExpectBuilder().withInputs(this.process.getInputStream()).withOutput(this.process.getOutputStream()).withExceptionOnFailure()
          .withTimeout(5, TimeUnit.SECONDS).build();
    } catch (IOException e) {
      this.process = null;
      this.expect = null;
      e.printStackTrace();
    }
  }

  public SensorDescription getSensorDescription() {
    return this.sd;
  }

  public void start() throws IOException {
    LOG.info("gatttool(" + this.sd.getName() + ") connecting to " + this.sd.getBdAddress());
    this.expect.sendLine(GATTTTOOL_INTERACTIVE);
    System.out.println(this.expect.expect(Matchers.regexp(PATTERN_DISCONNECTED)).group(0));
    this.expect.sendLine(CMD_CONNECT + " " + this.sd.getBdAddress());
    System.out.println(this.expect.expect(Matchers.regexp(PATTERN_CONNECTED)).group(0));
    LOG.info("gatttool(" + this.sd.getName() + ") connected");
  }

  public void listPrimaryServices() throws IOException {
    LOG.info("gatttool(" + this.sd.getName() + ") primary services");
    this.expect.sendLine(CMD_PRIMARY);
    try {
      // TODO
      System.out.println(this.expect.expect(Matchers.anyString()).getBefore());
      System.out.println(this.expect.expect(Matchers.anyString()).getBefore());
      System.out.println(this.expect.expect(Matchers.anyString()).getBefore());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void exit() throws IOException, InterruptedException {
    exitGatttool();
    exitShell();
  }

  private void exitGatttool() throws IOException {
    this.expect.sendLine(CMD_EXIT);
    System.out.println(this.expect.expect(Matchers.anyString()).getBefore());
    LOG.info("gatttool(" + this.sd.getName() + ") disconnected");
  }

  private void exitShell() throws IOException, InterruptedException {
    this.expect.sendLine(CMD_EXIT);
    this.expect.expect(Matchers.eof());
    this.process.waitFor();
    this.expect.close();
  }

}
