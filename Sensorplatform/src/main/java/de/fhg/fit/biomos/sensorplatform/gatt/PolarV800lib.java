package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * The Polar V800 is the wrist watch! Long press "back" to make the watch discoverable for a few seconds. The watch measures the pulse.
 *
 * @author Daniel Pyka
 *
 */
public abstract class PolarV800lib {
  //@formatter:off

  // 00:22:D0:A7:0A:46

  // primary
  public static final UUID GENERIC_ACCESS = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public static final UUID DEVICE_INFORMATION = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public static final UUID BATTERY = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");

  // characteristics
  public static final UUID UUID_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_DEVICE_NAME = "0x0004";

  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_APPEARANCE = "0x0006";

  public static final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x0008";

  public static final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MANUFACTURER_NAME_STRING = "0x000b";

  public static final UUID UUID_MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MODEL_NUMBER_STRING = "0x000d";

  public static final UUID UUID_SERIAL_NUMBER_STRING = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SERIAL_NUMBER_STRING = "0x000f";

  public static final UUID UUID_HARDWARE_REVISION_STRING = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_HARDWARE_REVISION_STRING = "0x0011";

  public static final UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_FIRMWARE_REVISION_STRING = "0x0013";

  public static final UUID UUID_SOFTWARE_REVISION_STRING = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SOFTWARE_REVISION_STRING = "0x0015";

  public static final UUID UUID_SYSTEM_ID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_SYSTEM_ID = "0x0017";

  public static final UUID UUID_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = UUID.fromString("00002a2a-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = "0x0019";

  public static final UUID UUID_PNP_ID = UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PNP_ID = "0x001b";

  public static final UUID UUID_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_BATTERY_LEVEL = "0x001e";


//@formatter:on

}
