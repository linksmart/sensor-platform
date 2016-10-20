package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Helper class for calculation and interpretation of 2G rssi related values.
 *
 * @author Daniel Pyka
 *
 */
public class GSM_GPRS_EDGE {

  public enum RSSI {
    EXCELLENT("excellent"), GOOD("good"), FAIR("fair"), POOR("poor"), NOSIGNAL("no signal"), INVALID("invalid value");

    private final String quality;

    private RSSI(String quality) {
      this.quality = quality;
    }

    @Override
    public String toString() {
      return this.quality;
    }
  }

  /**
   * Rate the signal strength rssi.
   *
   * @param rssiDBM
   *          rssi value in dBm
   * @return RSSI a short description
   */
  public static RSSI evaluateRSSI(int rssiDBM) {
    if (rssiDBM > -70) {
      return RSSI.EXCELLENT;
    } else if (rssiDBM < -70 && rssiDBM >= -85) {
      return RSSI.GOOD;
    } else if (rssiDBM < -85 && rssiDBM >= -100) {
      return RSSI.FAIR;
    } else if (rssiDBM < -100 && rssiDBM >= -110) {
      return RSSI.POOR;
    } else if (rssiDBM < -110 && rssiDBM >= -113) {
      return RSSI.NOSIGNAL;
    } else {
      return RSSI.INVALID;
    }
  }

  /**
   * Calculate the rssi value in dBm. The calculation is for 2G connections, but for convenience it is often used for 3G networks in the same way.
   *
   * @param asu
   *          raw asu value (0 to 31)
   * @return RSSI in dBm
   */
  public static int rssiASUtoDBM(int asu) {
    return (-113) + asu * 2;
  }

  /**
   * Calculate the percentage of the signal quality. The calculation is for 2G connections, but for convenience it is often used for 3G networks in the same
   * way.
   *
   * @param asu
   *          raw asu value (0 to 31)
   * @return RSSI in percentage
   */
  public static int rssiASUtoDBMpercent(int asu) {
    return Math.round(new Float(asu) / 31 * 100);
  }

}
