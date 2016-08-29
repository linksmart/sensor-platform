package de.fhg.fit.biomos.sensorplatform.gatt;

public interface PulseOximeterGattLibrary extends GattLibrary {

  public String getHandlePulseOximeterMeasurement();

  public String getHandlePulseOximeterNotification();

}
