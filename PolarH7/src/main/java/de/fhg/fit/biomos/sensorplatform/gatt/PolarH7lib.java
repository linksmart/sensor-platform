package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Polar H7 is the heart rate monitor for the chest strap. Skin contact with both electrodes to enable the device for a few seconds. The device indicates skin
 * contact and will shut down automatically after several seconds without skin contact.
 *
 * @author Daniel Pyka
 *
 */
public class PolarH7lib implements HeartRateGattLibrary {

  public static final String DEFAULT_BDADDRESS = "00:22:D0:AA:1F:B1";

  // primary
  public final UUID TODO = UUID.fromString("6217ff4b-fb31-1140-ad5a-a45545d7ecf3");

  // characteristics
  public final String HANDLE_DEVICE_NAME = "0x0003";

  public final String HANDLE_APPEARANCE = "0x0005";

  public final String HANDLE_PERIPHERAL_PRIVACY_FLAG = "0x0007";

  public final String HANDLE_RECONNECTION_ADDRESS = "0x0009";

  public final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x000b";

  public final String HANDLE_SERVICE_CHANGED = "0x000e";

  public final String HANDLE_HEART_RATE_MEASUREMENT = "0x0012";
  public final String HANDLE_HEART_RATE_NOTIFICATION = "0x0013";

  public final String HANDLE_BODY_SENSOR_LOCATION = "0x0015";

  public final String HANDLE_SYSTEM_ID = "0x0018";

  public final String HANDLE_MODEL_NUMBER_STRING = "0x001a";

  public final String HANDLE_SERIAL_NUMBER_STRING = "0x001c";

  public final String HANDLE_FIRMWARE_REVISION_STRING = "0x001e";

  public final String HANDLE_HARDWARE_REVISION_STRING = "0x0020";

  public final String HANDLE_SOFTWARE_REVISION_STRING = "0x0022";

  public final String HANDLE_MANUFACTURER_NAME_STRING = "0x0024";

  public final String HANDLE_BATTERY_LEVEL = "0x0027";

  public final UUID UUID_UNKNOWN_POLAR_1 = UUID.fromString("6217ff4c-c8ec-b1fb-1380-3ad986708e2db");
  public final String HANDLE_UNKNOWN_POLAR_1 = "0x002a";

  public final UUID UUID_UNKNOWN_POLAR_2 = UUID.fromString("6217ff4d-91bb-91d0-7e2a-7cd3bda8a1f3");
  public final String HANDLE_UNKNOWN_POLAR_2 = "0x002c";

  public PolarH7lib() {
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
