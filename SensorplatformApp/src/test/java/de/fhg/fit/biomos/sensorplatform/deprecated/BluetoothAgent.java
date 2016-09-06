package de.fhg.fit.biomos.sensorplatform.deprecated;

/**
 *
 * @author Daniel Pyka
 *
 */
@Deprecated
public enum BluetoothAgent {
  DISPLAYONLY("DisplayOnly"), DISPLAYYESNO("DisplayYesNo"), KEYBOARDDISPLAY("KeyboardDisplay"), KEYBOARDONLY("KeyboardOnly"), NOINPUTNOOUTPUT(
      "NoInputNoOutput"), ON("on"), OFF("off");

  private final String agent;

  @Deprecated
  private BluetoothAgent(String agent) {
    this.agent = agent;
  }

  @Override
  @Deprecated
  public String toString() {
    return this.agent;
  }

}
