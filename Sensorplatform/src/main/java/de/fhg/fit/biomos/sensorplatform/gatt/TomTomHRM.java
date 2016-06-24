package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Heart Rate Monitor from TomTom. Seems to be the same hardware as Adidas HRM.
 * Used UUIDs and handles are identical for both devices. Requires gatttool -t
 * random -b F4:2C:87:24:12:09 -I to connect?!
 *
 * @author Daniel Pyka
 *
 */
public class TomTomHRM {
  //@formatter:off

  // F4:2C:87:24:12:09 name is HRM (Ver0.4)

  // primary
  public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public static final UUID GENERIC_ACCESS = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public static final UUID DEVICE_INFORMATION = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public static final UUID HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
  public static final UUID BATTERY = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");

  // characteristics
  public static final UUID UUID_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_DEVICE_NAME = "0x0003";

  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_APPEARANCE = "0x0005";

  public static final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x0007";

  public static final UUID UUID_SERVICE_CHANGED = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SERVICE_CHANGED = "0x000b";

  public static final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MANUFACTURER_NAME_STRING = "0x000e";

  public static final UUID UUID_SYSTEM_ID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SYSTEM_ID = "0x0011";

  public static final UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_FIRMWARE_REVISION_STRING = "0x0013";

  public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_HEART_RATE_MEASUREMENT = "0x0017";
  public static final String HANDLE_HEART_RATE_NOTIFICATION = "0x0018"; // write 01:00 to enable

  public static final UUID UUID_BODY_SENSOR_LOCATION = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_BODY_SENSOR_LOCATION = "0x001a";

  public static final UUID UUID_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_BATTERY_LEVEL = "0x001d";

  //@formatter:on
}
