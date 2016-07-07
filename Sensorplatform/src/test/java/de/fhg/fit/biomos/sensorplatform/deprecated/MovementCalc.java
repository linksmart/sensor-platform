package de.fhg.fit.biomos.sensorplatform.deprecated;

public class MovementCalc {

  public static void main(String[] args) {
    // String data = "d1 ff ca 00 40 ff c6 03 1a ff 89 0e 48 02 e2 00 78 02";
    String data = "3f 00 4e 00 0a 00 84 03 71 00 79 0d 48 02 db 00 7a 02";
    // 3f004e000a0084037100790d4802db007a02
    data = data.replace(" ", "");
    System.out.println(data);

    // Gyro_x:
    float gx = Math.round((Integer.parseInt(data.substring(2, 4) + data.substring(0, 2), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    System.out.println(gx + "deg/s");
    // Gyro_y:
    float gy = Math.round((Integer.parseInt(data.substring(6, 8) + data.substring(4, 6), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    System.out.println(gy + "deg/s");
    // Gyro_z:
    float gz = Math.round((Integer.parseInt(data.substring(10, 12) + data.substring(8, 10), 16) * 1.0f) / (65536 / 500) * 100) / 100.0f;
    System.out.println(gz + "deg/s");

    // Acc_x:
    float accx = Math.round((Integer.parseInt(data.substring(14, 16) + data.substring(12, 14), 16) * 1.0f) / (32768 / 8) * 100) / 100.0f;
    System.out.println(accx + "G");
    // Acc_y:
    float accy = Math.round((Integer.parseInt(data.substring(18, 20) + data.substring(16, 18), 16) * 1.0f) / (32768 / 8) * 100) / 100.0f;
    System.out.println(accy + "G");
    // Acc_z:
    float accz = Math.round((Integer.parseInt(data.substring(22, 24) + data.substring(20, 22), 16) * 1.0f) / (32768 / 8) * 100) / 100.0f;
    System.out.println(accz + "G");

    // Mag_x:
    int magx = Integer.parseInt(data.substring(26, 28) + data.substring(24, 26), 16);
    System.out.println(magx + "uT");
    // Mag_y:
    int magy = Integer.parseInt(data.substring(30, 32) + data.substring(28, 30), 16);
    System.out.println(magy + "uT");
    // Mag_zz:
    int magz = Integer.parseInt(data.substring(34, 36) + data.substring(32, 34), 16);
    System.out.println(magz + "uT");
  }

}
