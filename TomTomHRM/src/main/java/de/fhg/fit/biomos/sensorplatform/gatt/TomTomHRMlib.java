package de.fhg.fit.biomos.sensorplatform.gatt;

/**
 * Heart Rate Monitor from TomTom. Seems to be the same hardware as Adidas HRM. Used UUIDs and handles are identical for both devices. Skin contact with both
 * electrodes to enable the device for a few seconds. The device indicates skin contact and will shut down automatically after several seconds without skin
 * contact.
 *
 * @author Daniel Pyka
 *
 */
@SuppressWarnings("unused")
public class TomTomHRMlib implements HeartRateGattLibrary {

  public static final String DEFAULT_BDADDRESS = "F4:2C:87:24:12:09";

  // characteristics
  private final String HANDLE_DEVICE_NAME = "0x0003";

  private final String HANDLE_APPEARANCE = "0x0005";

  private final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x0007";

  private final String HANDLE_SERVICE_CHANGED = "0x000b";

  private final String HANDLE_MANUFACTURER_NAME_STRING = "0x000e";

  private final String HANDLE_SYSTEM_ID = "0x0011";

  public final String HANDLE_FIRMWARE_REVISION_STRING = "0x0013";

  private final String HANDLE_HEART_RATE_MEASUREMENT = "0x0017";
  private final String HANDLE_HEART_RATE_NOTIFICATION = "0x0018";

  private final String HANDLE_BODY_SENSOR_LOCATION = "0x001a";

  private final String HANDLE_BATTERY_LEVEL = "0x001d";

  public TomTomHRMlib() {
  }

  @Override
  public String getHandleDeviceName() {
    return this.HANDLE_DEVICE_NAME;
  }

  @Override
  public String getHandleHeartRateMeasurement() {
    return this.HANDLE_HEART_RATE_MEASUREMENT;
  }

  @Override
  public String getHandleHeartRateNotification() {
    return this.HANDLE_HEART_RATE_NOTIFICATION;
  }

}
