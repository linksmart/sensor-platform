package de.fhg.fit.biomos.sensorplatform.tools;

/**
 * Used for debugging bluetooth controller.
 *
 * @author Daniel Pyka
 *
 */
public interface Hciconfig {

  public String getLocalBDaddress();

  public void up();

  public void down();

}
