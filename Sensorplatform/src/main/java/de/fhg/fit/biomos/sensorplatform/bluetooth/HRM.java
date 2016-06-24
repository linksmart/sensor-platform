package de.fhg.fit.biomos.sensorplatform.bluetooth;

import java.util.regex.Pattern;

/**
 *
 * @author Daniel Pyka
 *
 */
public class HRM {
  public static final byte UINT16 = 1;
  public static final byte RR_INTERVAL_AVAILABLE = 1 << 4;
  public static final byte SKIN_CONTACT_SUPPORTED = 1 << 2;
  public static final byte SKIN_CONTACT_DETECTED = 1 << 1;
  public static final byte ENERGY_EXPENDED = 1 << 3;

  public static final Pattern PATTERN_RR_DATA = Pattern.compile("(\\w{2}\\s\\w{2})+");
}
