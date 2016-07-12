package de.fhg.fit.biomos.sensorplatform.deprecated;

public class AmbientLightCalc {

  public static void main(String[] args) {
    String data = "f4 59";

    data = data.replace(" ", "");
    String val = data.substring(2, 4) + data.substring(0, 2);

    int raw = Integer.parseInt(val, 16);

    System.out.println(raw);

    int m = raw & 0x0FFF;
    int e = (raw & 0xF000) >>> 12;

    System.out.println(m);
    System.out.println(e);

    float light = (float) (m * (0.01 * Math.pow(2.0, e)));

    System.out.println(String.valueOf(light));

    // m = rawData & 0x0FFF;
    // e = (rawData & 0xF000) >> 12;
    //
    // return m * (0.01 * pow(2.0,e));

  }

}
