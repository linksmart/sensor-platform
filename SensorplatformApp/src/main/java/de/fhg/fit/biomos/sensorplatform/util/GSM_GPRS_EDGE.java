package de.fhg.fit.biomos.sensorplatform.util;

public class GSM_GPRS_EDGE {

  public enum RSSI {
    NONETWORK("no network"), GSM("GSM, connection losses expected"), GPRS_SLOW("GPRS, very low datarate"), GPRS_STABLE("GPRS, stable"), EDGE_STABLE(
        "EDGE, stable"), EDGE_GOOD("EDGE, good"), EEDGE("E-EDGE, very good"), INVALID("invalid value");

    private final String type;

    private RSSI(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return this.type;
    }
  }

  public static RSSI evaluateRSSI(int asuDBM) {
    if (asuDBM == -113) {
      return RSSI.NONETWORK;
    } else if (asuDBM > -113 && asuDBM <= 108) {
      return RSSI.GSM;
    } else if (asuDBM > -108 && asuDBM <= -102) {
      return RSSI.GPRS_SLOW;
    } else if (asuDBM > -102 && asuDBM <= -92) {
      return RSSI.GPRS_STABLE;
    } else if (asuDBM > -92 && asuDBM <= -82) {
      return RSSI.EDGE_STABLE;
    } else if (asuDBM > -82 && asuDBM <= -60) {
      return RSSI.EDGE_GOOD;
    } else if (asuDBM > -60 && asuDBM <= -51) {
      return RSSI.EEDGE;
    } else {
      return RSSI.INVALID;
    }
  }

}
