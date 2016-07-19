package de.fhg.fit.biomos.sensorplatform.util;

public enum SensorName {
  CC2650("Texas Instruments CC2650 SensorTag"), PolarH7("Polar H7"), TomTomHRM("TomTom HRM"), AdidasHRM("Adidas HRM"), PolarV800("Polar V800");

  private final String type;

  private SensorName(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return this.type;
  }

}