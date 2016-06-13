package de.fhg.fit.biomos.sensorplatform.util;

public class SensorDescription {

  public static enum SENSORTYPE {HEARTRATE, SPO2, TEMPERATURE, STEPCOUNTER, UNDEFINED};
  
  private SENSORTYPE type;
  private String name;
  private String bdAddress;
  
  public SensorDescription(SENSORTYPE type, String name, String bdAddress) {
    this.type = type;
    this.name = name;
    this.bdAddress = bdAddress;
  }
  
  public SensorDescription(String name, String bdAddress) {
    this.type = SENSORTYPE.UNDEFINED;
    this.name = name;
    this.bdAddress = bdAddress;
  }
  
  public SensorDescription(SENSORTYPE type, String bdAddress) {
    this.type = type;
    this.name = "unknown";
    this.bdAddress = bdAddress;
  }

  public SENSORTYPE getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getBdAddress() {
    return bdAddress;
  }

  public void setType(SENSORTYPE type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBdAddress(String bdAddress) {
    this.bdAddress = bdAddress;
  }
  
  @Override
  public String toString(){
    return "{\"type\":\"" + this.type + "\",\"name\":\"" + this.name + "\",\"bdaddress\":\"" + bdAddress + "\"}";
  }
}
