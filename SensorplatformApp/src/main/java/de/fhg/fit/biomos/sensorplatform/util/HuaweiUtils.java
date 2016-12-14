package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Helper class for firmware specific information.
 *
 * @author Daniel Pyka
 *
 */
public class HuaweiUtils {

  public static final String CSNR_QUERY = "AT^CSNR?\r";
  public static final String SYSINFO_QUERY = "AT^SYSINFO\r";

  public static final String SYSTEM_MODE_0 = "No services";
  public static final String SYSTEM_MODE_3 = "GSM/GPRS mode";
  public static final String SYSTEM_MODE_4 = "HDR mode";
  public static final String SYSTEM_MODE_5 = "WCDMA mode";
  public static final String SYSTEM_MODE_6 = "GPS mode";
  public static final String SYSTEM_MODE_7 = "GSM/WCDMA mode";
  public static final String SYSTEM_MODE_15 = "TD-SCDMA mode";

  public static final String SYSTEM_SUBMODE_0 = "No services";
  public static final String SYSTEM_SUBMODE_1 = "GSM mode";
  public static final String SYSTEM_SUBMODE_2 = "GPRS mode";
  public static final String SYSTEM_SUBMODE_3 = "EDGE mode";
  public static final String SYSTEM_SUBMODE_4 = "WCDMA mode";
  public static final String SYSTEM_SUBMODE_5 = "HSDPA mode";
  public static final String SYSTEM_SUBMODE_6 = "HSUPA mode";
  public static final String SYSTEM_SUBMODE_7 = "HSUPA and HSDPA mode";
  public static final String SYSTEM_SUBMODE_8 = "TD_SCDMA mode";
  public static final String SYSTEM_SUBMODE_9 = "HSPA+ mode";
  public static final String SYSTEM_SUBMODE_17 = "HSPA+(64QAM) mode";
  public static final String SYSTEM_SUBMODE_18 = "HSPA+(MIMO) mode";

  /**
   * Get the name of the mode the surf stick is in, represented by an ID.
   *
   * @param modeID
   *          mode id from SYSINFO command
   * @return String name of the mode
   */
  public static String getSystemModeName(int modeID) {
    switch (modeID) {
      case 0:
        return SYSTEM_MODE_0;
      case 3:
        return SYSTEM_MODE_3;
      case 4:
        return SYSTEM_MODE_4;
      case 5:
        return SYSTEM_MODE_5;
      case 7:
        return SYSTEM_MODE_7;
      case 15:
        return SYSTEM_MODE_15;
      default:
        return "invalid";
    }
  }

  /**
   * Get the name of the sub mode the surf stick is in, represented by an ID.
   *
   * @param submodeID
   *          sub mode id from SYSINFO command
   * @return String name of the sub mode
   */
  public static String getSystemSubmodeName(int submodeID) {
    switch (submodeID) {
      case 0:
        return SYSTEM_SUBMODE_0;
      case 1:
        return SYSTEM_SUBMODE_1;
      case 2:
        return SYSTEM_SUBMODE_2;
      case 3:
        return SYSTEM_SUBMODE_3;
      case 4:
        return SYSTEM_SUBMODE_4;
      case 5:
        return SYSTEM_SUBMODE_5;
      case 6:
        return SYSTEM_SUBMODE_6;
      case 7:
        return SYSTEM_SUBMODE_7;
      case 8:
        return SYSTEM_SUBMODE_8;
      case 9:
        return SYSTEM_SUBMODE_9;
      case 10:
        return SYSTEM_SUBMODE_17;
      case 11:
        return SYSTEM_SUBMODE_18;
      default:
        return "invalid";
    }
  }
}
