package de.fhg.fit.biomos.sensorplatform.deprecated;

/**
 * Controller class for bluetoothctl command line tool. Bluetoothctl is interactive.
 *
 * @author Daniel Pyka
 *
 */
@Deprecated
public interface Bluetoothctl {
  @Deprecated
  public void init();

  @Deprecated
  public String getControllerBDaddress();

  @Deprecated
  public void powerOn();

  @Deprecated
  public void powerOff();

  @Deprecated
  public void scanOn(int timeout);

  @Deprecated
  public void scanOff();

  @Deprecated
  public void connect(String bdAddress);

  @Deprecated
  public void disconnect();

  @Deprecated
  public void pair();

  @Deprecated
  public void remove(String bdAddress);

}
