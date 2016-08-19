package de.fhg.fit.biomos.sensorplatform.util;

/**
 *
 * @author Daniel Pyka
 *
 */
public enum BluetoothAgent {
  DISPLAYONLY("DisplayOnly"), DISPLAYYESNO("DisplayYesNo"), KEYBOARDDISPLAY("KeyboardDisplay"), KEYBOARDONLY("KeyboardOnly"), NOINPUTNOOUTPUT(
      "NoInputNoOutput"), ON("on"), OFF("off");

  private final String agent;

  private BluetoothAgent(String agent) {
    this.agent = agent;
  }

  @Override
  public String toString() {
    return this.agent;
  }

}
