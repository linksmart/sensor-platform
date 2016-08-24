package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.util.List;

public interface Hcitool {

  public void connect();

  public void pair();

  public void disconnect();

  public void scanFor(String bdAddress);

  public List<String> getFoundDevices();

}
