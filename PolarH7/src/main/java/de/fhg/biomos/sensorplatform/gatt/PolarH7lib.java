package de.fhg.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Polar H7 is the heart rate monitor for the chest strap. Skin contact with both electrodes to enable the device for a few seconds. The device indicates skin
 * contact and will shut down automatically after several seconds without skin contact.
 *
 * @author Daniel Pyka
 *
 */
public abstract class PolarH7lib {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS = "00:22:D0:AA:1F:B1";

  // primary
  public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public static final UUID GENERIC_ACCESS = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public static final UUID HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
  public static final UUID DEVICE_INFORMATION = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public static final UUID BATTERY = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
  public static final UUID TODO = UUID.fromString("6217ff4b-fb31-1140-ad5a-a45545d7ecf3");

  // characteristics
  public static final UUID UUID_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_DEVICE_NAME = "0x0003";

  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_APPEARANCE = "0x0005";

  public static final UUID UUID_PERIPHERAL_PRIVACY_FLAG = UUID.fromString("00002a02-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PERIPHERAL_PRIVACY_FLAG = "0x0007";

  public static final UUID UUID_RECONNECTION_ADDRESS = UUID.fromString("00002a03-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_RECONNECTION_ADDRESS = "0x0009";

  public static final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x000b";

  public static final UUID UUID_SERVICE_CHANGED = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SERVICE_CHANGED = "0x000e";

  public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_HEART_RATE_MEASUREMENT = "0x0012";
  public static final String HANDLE_HEART_RATE_NOTIFICATION = "0x0013";

  public static final UUID UUID_BODY_SENSOR_LOCATION = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_BODY_SENSOR_LOCATION = "0x0015";

  public static final UUID UUID_SYSTEM_ID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SYSTEM_ID = "0x0018";

  public static final UUID UUID_MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MODEL_NUMBER_STRING = "0x001a";

  public static final UUID UUID_SERIAL_NUMBER_STRING = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SERIAL_NUMBER_STRING = "0x001c";

  public static final UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_FIRMWARE_REVISION_STRING = "0x001e";

  public static final UUID UUID_HARDWARE_REVISION_STRING = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_HARDWARE_REVISION_STRING = "0x0020";

  public static final UUID UUID_SOFTWARE_REVISION_STRING = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SOFTWARE_REVISION_STRING = "0x0022";

  public static final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MANUFACTURER_NAME_STRING = "0x0024";

  public static final UUID UUID_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_BATTERY_LEVEL = "0x0027";

  public static final UUID UUID_UNKNOWN_POLAR_1 = UUID.fromString("6217ff4c-c8ec-b1fb-1380-3ad986708e2db");
  public static final String HANDLE_UNKNOWN_POLAR_1 = "0x002a";

  public static final UUID UUID_UNKNOWN_POLAR_2 = UUID.fromString("6217ff4d-91bb-91d0-7e2a-7cd3bda8a1f3");
  public static final String HANDLE_UNKNOWN_POLAR_2 = "0x002c";

  //@formatter:on

}
