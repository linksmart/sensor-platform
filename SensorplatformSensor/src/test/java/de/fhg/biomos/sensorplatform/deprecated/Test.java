package de.fhg.biomos.sensorplatform.deprecated;

import java.util.ArrayList;
import java.util.List;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

public abstract class Test {

  public static void main(String[] args) {
    // CC2650Sample s = new CC2650Sample("2016-07-20T09:40:30:600Z", "11:22:33:44:55:66", false);
    // s.setTemperatureMeasurement(25.5f, 30.0f);
    // System.out.println(s.toString());
    // s.setHumidityMeasurement(28.6f, 33f);
    // System.out.println(s.toString());
    // s.setAmbientlightMeasurement(500f);
    // System.out.println(s.toString());
    // s.setPressureMeasurement(50f, 1000f);
    // System.out.println(s.toString());
    // s.setMovementMeasurement(new float[] { 10, 20, 30 }, new float[] { 1, 2, 3 }, new float[] { 100, 200, 300 });
    // System.out.println(s.toString());
    //
    // System.out.println(s.toString());

    HeartRateSample hrs = new HeartRateSample("2016-07-20T20:20:20:600Z", "AA:BB:CC:DD:EE:FF", false);
    hrs.setHeartRate(120);
    List<Integer> l = new ArrayList<Integer>();
    l.add(500);
    l.add(501);
    hrs.setRRinterval(l);

    System.out.println(hrs.toString());
  }

}
