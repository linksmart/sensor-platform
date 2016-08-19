package de.fhg.fit.biomos.sensorplatform.tools;

public interface Hcitool {

  public String connect(String bdAddress);

  public void pair(String bdAddress);

  public void disconnect(String handle);

  public void scan();

  public String getFoundDevices();

}
