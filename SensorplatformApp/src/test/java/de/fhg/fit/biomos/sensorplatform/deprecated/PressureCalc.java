package de.fhg.fit.biomos.sensorplatform.deprecated;

public class PressureCalc {

  public static void main(String[] args) {
    String data = "9b 0a 00 4b 88 01";
    data = data.replace(" ", "");

    int rawTemp = Integer.parseInt(data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2), 16);
    int rawPress = Integer.parseInt(data.substring(10, 12) + data.substring(8, 10) + data.substring(6, 8), 16);

    Float temp = rawTemp / 100.0f;
    Float press = rawPress / 100.0f;

    System.out.println(temp + "°C");
    System.out.println(press + "hPa");
  }

}
