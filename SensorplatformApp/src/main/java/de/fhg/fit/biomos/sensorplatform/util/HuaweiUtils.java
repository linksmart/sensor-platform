package de.fhg.fit.biomos.sensorplatform.util;

public class HuaweiUtils {

  public static final String CSNR_QUERY = "AT^CSNR?\r";
  public static final String SYSINFO_QUERY = "AT^SYSINFO\r";

  public static final String SYSTEM_MODE_0 = "No services";
  public static final String SYSTEM_MODE_3 = "GSM/GPRS mode";
  public static final String SYSTEM_MODE_5 = "WCDMA mode";
  public static final String SYSTEM_MODE_7 = "GSM/WCDMA mode";

  public static final String SYSTEM_SUBMODE_0 = "No services";
  public static final String SYSTEM_SUBMODE_1 = "GSM mode";
  public static final String SYSTEM_SUBMODE_2 = "GPRS mode";
  public static final String SYSTEM_SUBMODE_3 = "EDGE mode";
  public static final String SYSTEM_SUBMODE_4 = "WCDMA mode";
  public static final String SYSTEM_SUBMODE_5 = "HSDPA mode";
  public static final String SYSTEM_SUBMODE_6 = "HSUPA mode";
  public static final String SYSTEM_SUBMODE_7 = "HSUPA and HSDPA mode";

  public static String getSystemModeName(String modeID) {
    switch (modeID) {
      case "0":
        return SYSTEM_MODE_0;
      case "3":
        return SYSTEM_MODE_3;
      case "5":
        return SYSTEM_MODE_5;
      case "7":
        return SYSTEM_MODE_7;
      default:
        return "invalid";
    }
  }

  public static String getSystemSubmodeName(String submodeID) {
    switch (submodeID) {
      case "0":
        return SYSTEM_SUBMODE_0;
      case "1":
        return SYSTEM_SUBMODE_1;
      case "2":
        return SYSTEM_SUBMODE_2;
      case "3":
        return SYSTEM_SUBMODE_3;
      case "4":
        return SYSTEM_SUBMODE_4;
      case "5":
        return SYSTEM_SUBMODE_5;
      case "6":
        return SYSTEM_SUBMODE_6;
      case "7":
        return SYSTEM_SUBMODE_7;
      default:
        return "invalid";
    }
  }
}
