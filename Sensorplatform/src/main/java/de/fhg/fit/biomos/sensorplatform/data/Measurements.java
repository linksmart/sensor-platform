package de.fhg.fit.biomos.sensorplatform.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Measurements {
  
  private static final SimpleDateFormat formatter = new SimpleDateFormat("H:mm:ss:SSS");
  
  private final String sensorName;
  private final String macAddress;
  private List<String> samplesList = new ArrayList<>();
  private int listIndex = 0;

  public Measurements(String sensorName, String macAddress) {
    this.sensorName = sensorName;
    this.macAddress = macAddress;
  }
  
  public String getSensorName() {
    return sensorName;
  }

  public String getMacAddress() {
    return macAddress;
  }
  
  public int addSampleToList(String sample){
    samplesList.add(formatter.format(Calendar.getInstance().getTime()) + " " + sample);
    return ++listIndex;
  }
  
  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append(sensorName).append("\n").append(macAddress).append("\n");
    for (String sample : samplesList) {
      sb.append(sample).append("\n");
    }
    return sb.toString();
  }
  
}
