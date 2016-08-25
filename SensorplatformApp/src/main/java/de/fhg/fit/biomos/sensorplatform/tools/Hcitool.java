package de.fhg.fit.biomos.sensorplatform.tools;

import java.util.List;

import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

public interface Hcitool {

  public String connect(String bdAddress);

  public void pair(String bdAddress);

  public void disconnect(String handle);

  public List<DetectedDevice> scan();

  public List<DetectedDevice> getDetectedDevices();

}
