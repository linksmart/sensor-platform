package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.SampleCollector;
import de.fhg.fit.biomos.sensorplatform.sensor.Sensor;
import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;

/**
 * Defines an Interface for interacting with the Sensorplatform. This API may be expanded, for example: Add reading of battery status, get intended position of
 * the sensor on the human body, ... The Sensorplatform in its current state utilises only a small subset of what it is possible or supported by the Bluetooth
 * specification.
 *
 * @author Daniel Pyka
 *
 */
public interface SensorWrapper<T extends Sensor<?>> {

  /**
   * Expose the sensor object.
   *
   * @return Sensor the sensor object
   */
  public T getSensor();

  /**
   * Expose the gattool.
   *
   * @return Gatttool the object which handles the gatttool process
   */
  public Gatttool getGatttool();

  public SampleCollector getSampleCollector();

  /**
   * Start getting notification data from the sensor.
   */
  public void enableLogging();

  /**
   * Stop getting notification data from the sensor.
   */
  public void disableLogging();

  /**
   * Get a meaningful time stamp when the last notification has arrived.
   *
   * @return long time stamp milliseconds
   */
  public long getLastNotifactionTimestamp();

  /**
   * Notifiy the observer when a new sensor notification has arrived.
   *
   * @param observable
   *          source object of this notification
   * @param handle
   *          hexadecimal handle number of the notification as an identifier
   * @param rawHexValues
   *          hexadecimal values of the notification delivered by gatttool
   */
  public void newNotificationData(Gatttool observable, String handle, String rawHexValues);

}
