package de.fhg.fit.biomos.sensorplatform.deprecated;

public class TemperatureCalc {

  public static void main(String[] args) {
    // String data = "54 0a c8 0d";
    String data = "84 15 e8 0c";
    float scale = 0.03125F;

    data = data.replace(" ", ""); // 540a c80d
    String val1 = data.substring(0, 4); // 540a
    val1 = (val1 + val1.substring(0, 2)).substring(2, 6); // 540a54 -> 0a54
    String val2 = data.substring(4, 8); // c80d
    val2 = (val2 + val2.substring(0, 2)).substring(2, 6); // c80dc8 -> 0dc8

    int v1 = (Integer.parseInt(val1, 16)) >>> 2;
    int v2 = (Integer.parseInt(val2, 16)) >>> 2;

    double object = Math.round(v1 * scale * 10) / 10.0;
    double ambience = Math.round(v2 * scale * 10) / 10.0;
    System.out.println("Object: " + object + "°C");
    System.out.println("Die: " + ambience + "°C");
  }

}
