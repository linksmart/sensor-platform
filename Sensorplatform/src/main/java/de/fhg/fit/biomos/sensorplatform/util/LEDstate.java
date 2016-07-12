package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Defines specific key words which are used as input parameters for the shell script for controlling the onboard LED.
 *
 * @see {@link de.fhg.fit.biomos.sensorplatform.main.LEDcontrol}
 *
 * @author Daniel
 *
 */
public enum LEDstate {
  BLINK("blink"), HEARTBEAT("heartbeat"), ERROR("error"), OFF("off");

  private final String state;

  private LEDstate(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return this.state;
  }

}
