package de.fhg.fit.biomos.sensorplatform.deprecated;

public class HumidityTest {

  public static void main(String[] args) {
    float scale = 0.03125F;

    String data = "6c 68 e0 67";

    data = data.replace(" ", ""); // 54 0a c8 0d -> 540ac80d
    String val1 = data.substring(0, 4);
    val1 = (val1 + val1.substring(0, 2)).substring(2, 6); // 540a54 -> 0a54
    String val2 = data.substring(4, 8);
    val2 = (val2 + val2.substring(0, 2)).substring(2, 6); // c80dc8 -> 0dc8

    int v1 = (Integer.parseInt(val1, 16));
    int v2 = (Integer.parseInt(val2, 16));

    // temp = ((double)(int16_t)rawTemp / 65536)*165 - 40;
    // hum = ((double)rawHum / 65536)*100;

    double temp = ((double) v1 / 65536) * 165 - 40;
    double hum = ((double) v2 / 65536) * 100;

    System.out.println("temp: " + temp);
    System.out.println("hum: " + hum);
  }
}
