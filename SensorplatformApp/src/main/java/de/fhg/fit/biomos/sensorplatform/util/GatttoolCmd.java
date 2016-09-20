package de.fhg.fit.biomos.sensorplatform.util;

import java.util.regex.Pattern;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class GatttoolCmd {
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

  public static final Pattern NOTIFICATION_DATA = Pattern.compile("Notification handle = (\\dx\\d{4}) value: (.+)$");
}
