package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Bean for storing three parameters (RSCP, Ec/Io and the resulting RSSI) to judge signal quality.
 *
 * @author Daniel Pyka
 *
 */
public class SignalQualityBean {

  public static final int RSCP_DEFAULT = -145;
  public static final int ECIO_DEFAULT = -32;

  private final int rscp;
  private final int ecio;
  private final int rssi;

  public SignalQualityBean(int rscp, int ecio) {
    this.rscp = rscp;
    this.ecio = ecio;
    this.rssi = rscp - ecio;
  }

  public int getRSCP() {
    return this.rscp;
  }

  public int getECIO() {
    return this.ecio;
  }

  public int getRSSI() {
    return this.rssi;
  }

}
