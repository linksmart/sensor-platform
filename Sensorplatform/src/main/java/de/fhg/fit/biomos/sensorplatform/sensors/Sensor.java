package de.fhg.fit.biomos.sensorplatform.sensors;

import java.io.BufferedWriter;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class Sensor implements SensorCommands {

  private final String name;
  private final String bdaddress;

  protected BufferedWriter bw = null;

  public Sensor(String name, String bdaddress) {
    this.name = name;
    this.bdaddress = bdaddress;
  }

  public String getName() {
    return this.name;
  }

  public String getBdaddress() {
    return this.bdaddress;
  }

  /**
   * Link the specific sensor to the input stream of a gatttool to send commands to the physical sensor.
   *
   * @param bw
   */
  public void hook(BufferedWriter bw) {
    this.bw = bw;
  }

  public void unhook() {
    this.bw = null;
  }

}
