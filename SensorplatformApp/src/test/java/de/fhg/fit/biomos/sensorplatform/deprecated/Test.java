package de.fhg.fit.biomos.sensorplatform.deprecated;

public class Test {

  public static void main(String[] args) throws InterruptedException {
    boolean[] b = { true, true, false, true, false };
    while (true) {
      if (b[0]) {
        System.out.println("b0");
        continue;
      }
      if (b[1]) {
        System.out.println("b1");
        continue;
      }
      if (b[2]) {
        System.out.println("b2");
        continue;
      }
      if (b[3]) {
        System.out.println("b3");
        continue;
      }
      if (b[4]) {
        System.out.println("b4");
        continue;
      }
      System.out.println("end");
      Thread.sleep(3000);
    }
  }

}
