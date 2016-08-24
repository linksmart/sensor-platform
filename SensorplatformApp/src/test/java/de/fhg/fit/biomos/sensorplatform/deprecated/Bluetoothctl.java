package de.fhg.fit.biomos.sensorplatform.deprecated;

/**
 * Controller class for bluetoothctl command line tool. Bluetoothctl is interactive.
 *
 * @author Daniel Pyka
 *
 */
public interface Bluetoothctl {

  public void init();

  public String getControllerBDaddress();

  public void powerOn();

  public void powerOff();

  public void scanOn(int timeout);

  public void scanOff();

  public void connect(String bdAddress);

  public void disconnect();

  public void pair();

  public void remove(String bdAddress);

}
