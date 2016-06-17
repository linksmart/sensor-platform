package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexTest {

  private static final byte UINT16 = 1;
  private static final byte RR_INTERVAL_AVAILABLE = 1 << 4;
  private static final byte SKIN_CONTACT_SUPPORTED = 1 << 2;
  private static final byte SKIN_CONTACT_DETECTED = 1 << 1;
  private static final byte ENERGY_EXPENDED = 1 << 3;

  private static final Pattern PATTERN_HRM = Pattern.compile("(\\w{2}\\s\\w{2})+");

  // some tests for HRM data interpretation from gatttool
  public static void main(String[] args) {

    String data = "16 46 74 03 4A 03 55 03";
    // String data = "04 46 74 03 4A 03 55 03";
    // String data = "06 46 74 03 4A 03 55 03";
    // String data = "17 00 46 74 03 4A 03 55 03";
    byte config = Byte.parseByte(data.substring(0, 2), 16);
    int heartrate = 0;
    String rrintervals = "";
    boolean rravailable = false;

    if ((config & RR_INTERVAL_AVAILABLE) == RR_INTERVAL_AVAILABLE) {
      System.out.println("rr value(s) available");
      rravailable = true;
    } else {
      System.out.println("no rr");
      rravailable = false;
    }

    if ((config & UINT16) == UINT16) {
      System.out.println("16 bit heart rate value");
      if (rravailable) {
        rrintervals = data.substring(9);
        System.out.println("rr interval(s): " + rrintervals);
      }
      heartrate = Integer.parseInt(data.substring(3, 8).replace(" ", ""), 16);
    } else {
      System.out.println("8 bit heart rate value");
      if (rravailable) {
        rrintervals = data.substring(6);
        System.out.println("rr interval(s): " + rrintervals);
      }
      heartrate = Integer.parseInt(data.substring(3, 5), 16);
    }
    System.out.println(heartrate + " Hz");

    if ((config & SKIN_CONTACT_SUPPORTED) == SKIN_CONTACT_SUPPORTED) {
      System.out.println("skin contact supported");
      if ((config & SKIN_CONTACT_DETECTED) == SKIN_CONTACT_DETECTED) {
        System.out.println("skin contact detected");
      } else {
        System.out.println("no skin contact detected");
      }
    } else {
      System.out.println("skin contact not supported");
    }

    if ((config & ENERGY_EXPENDED) == ENERGY_EXPENDED) {
      System.out.println("energy expended data");
    } else {
      System.out.println("no energy expended data");
    }

    Matcher m = PATTERN_HRM.matcher(rrintervals);
    int i = 1;
    while (m.find()) {
      String tmp = m.group(0);
      tmp = tmp + tmp.substring(0, 2);
      tmp = tmp.substring(3);
      System.out.println("rr-interval " + i++ + ": " + Integer.parseInt(tmp, 16) + " bpm/ms");
    }

    // System.out.println(config);

  }

}
