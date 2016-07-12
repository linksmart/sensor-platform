package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Texas Instruments SensorTag CC650</br>
 * This sensor provides measurements for the following parameters: temperature, humidity, ambientlight, pressure, and movement. Press both buttons on the sides
 * to wake it up from standby mode for several minutes. The LED will blink in a fixed interval during this time. Afterwards, the SensorTag returns to power
 * saving mode again. The LED does not blink if any device is connected the SensorTag.
 *
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @author Daniel Pyka
 *
 */
public abstract class CC2650lib {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS = "A0:E6:F8:B6:37:05";

  // primary services
  public static final UUID GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public static final UUID DEVICE_INFORMATION = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public static final UUID TEMPERATURE = UUID.fromString("f000aa00-0451-4000-b000-000000000000");
  public static final UUID HUMIDITY = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
  public static final UUID PRESSURE = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
  public static final UUID MOVEMENT = UUID.fromString("f000aa80-0451-4000-b000-000000000000");
  public static final UUID AMBIENTLIGHT = UUID.fromString("f000aa70-0451-4000-b000-000000000000");

  public static final UUID KEYS_STATUS = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

  public static final UUID IO_SERVICE = UUID.fromString("f000aa64-0451-4000-b000-000000000000");
  public static final UUID REGISTER_SERVICE = UUID.fromString("f000ac00-0451-4000-b000-000000000000");
  public static final UUID UUID_SENSORTAG_CONNECTION_CONTROL = UUID.fromString("f000ccc0-0451-4000-b000-000000000000");
  public static final UUID UUID_OVER_THE_AIR_UPDATE = UUID.fromString("f000ffc0-0451-4000-b000-000000000000");

  // characteristics
  public static final UUID UUID_DEVICENAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"); //  53 65 6e 73 6f 72 54 61 67 20 32 2e 30 (SensorTag 2.0)
  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb"); // 00 00
  public static final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb"); // 50 00 a0 00 00 00 e8 03

  public static final UUID UUID_SERVICE_CHANGED = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb"); // no read

  public static final UUID UUID_SYSTEM_ID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb"); // 05 37 b6 00 00 f8 e6 a0
  public static final UUID UUID_MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb"); //  43 43 32 36 35 30 20 53 65 6e 73 6f 72 54 61 67 (CC2650 SensorTag)
  public static final UUID UUID_SERIAL_NUMBER_STRING = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb"); // 4e 2e 41 2e (N.A.)
  public static final UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb"); // 31 2e 32 30 20 28 4a 75 6c 20 32 30 20 32 30 31 35 29 (1.20 (Jul 20 2015))
  public static final UUID UUID_HARDWARE_REVISION_STRING = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb"); //  50 43 42 20 31 2e 32 2f 31 2e 33 (PCB 1.2/1.3)
  public static final UUID UUID_SOFTWARE_REVISION_STRING = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb"); // 4e 2e 41 2e (N.A.)
  public static final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"); // 54 65 78 61 73 20 49 6e 73 74 72 75 6d 65 6e 74 73 (Texas Instruments)
  public static final UUID UUID_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = UUID.fromString("00002a2a-0000-1000-8000-00805f9b34fb"); // FE 00 65 78 70 65 72 69 6d 65 6e 74 61 6c (FE 00 experimental)
  public static final UUID UUID_PNP_ID = UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb"); // 01 0d 00 00 00 10 01

  public static final UUID UUID_IR_TEMPERATURE_VALUE = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
  public static final UUID UUID_IR_TEMPERATURE_ENABLE = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
  public static final UUID UUID_IR_TEMPERATURE_PERIOD = UUID.fromString("f000aa03-0451-4000-b000-000000000000");

  public static final UUID UUID_HUMIDITY_VALUE = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
  public static final UUID UUID_HUMIDITY_ENABLE = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
  public static final UUID UUID_HUMIDITY_PERIOD = UUID.fromString("f000aa23-0451-4000-b000-000000000000");

  public static final UUID UUID_PRESSURE_VALUE = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
  public static final UUID UUID_PRESSURE_ENABLE = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
  public static final UUID UUID_PRESSURE_PERIOD = UUID.fromString("f000aa44-0451-4000-b000-000000000000");

  public static final UUID UUID_MOVEMENT_VALUE = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
  public static final UUID UUID_MOVEMENT_ENABLE = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
  public static final UUID UUID_MOVEMENT_PERIOD = UUID.fromString("f000aa83-0451-4000-b000-000000000000");

  public static final UUID UUID_AMBIENTLIGHT_VALUE = UUID.fromString("f000aa71-0451-4000-b000-000000000000");
  public static final UUID UUID_AMBIENTLIGHT_ENABLE = UUID.fromString("f000aa72-0451-4000-b000-000000000000");
  public static final UUID UUID_AMBIENTLIGHT_PERIOD = UUID.fromString("f000aa73-0451-4000-b000-000000000000");

  public static final UUID UUID_KEYS_STATUS = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

  public static final UUID UUID_IO_DATA = UUID.fromString("f000aa65-0451-4000-b000-000000000000");
  public static final UUID UUID_IO_CONFIG = UUID.fromString("f000aa66-0451-4000-b000-000000000000");

  public static final UUID UUID_REGISTER_DATA = UUID.fromString("f000ac01-0451-4000-b000-000000000000");
  public static final UUID UUID_REGISTER_ADDRESS = UUID.fromString("f000ac02-0451-4000-b000-000000000000");
  public static final UUID UUID_REGISTER_DEVICE_ID = UUID.fromString("f000ac03-0451-4000-b000-000000000000");

  public static final UUID UUID_CONNECTION_PARAMETERS = UUID.fromString("f000ccc1-0451-4000-b000-000000000000");
  public static final UUID UUID_REQUEST_CONNECTION_PARAMETERS = UUID.fromString("f000ccc2-0451-4000-b000-000000000000");
  public static final UUID UUID_DISCONNECT_REQUEST = UUID.fromString("f000ccc3-0451-4000-b000-000000000000");

  public static final UUID UUID_OAD_IMAGE_IDENTIFY = UUID.fromString("f000ffc1-0451-4000-b000-000000000000");
  public static final UUID UUID_OAD_IMAGE_BLOCK = UUID.fromString("f000ffc2-0451-4000-b000-000000000000");

  public static final String HANDLE_DEVICE_NAME = "0x0003"; //  53 65 6e 73 6f 72 54 61 67 20 32 2e 30 (SensorTag 2.0)
  public static final String HANDLE_APPEARANCE = "0x0005"; // 00 00
  public static final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x0007"; // 50 00 a0 00 00 00 e8 03

  public static final String HANDLE_SERVICE_CHANGED = "0x000a"; // no read

  public static final String HANDLE_SYSTEM_ID = "0x000e"; // 05 37 b6 00 00 f8 e6 a0
  public static final String HANDLE_MODEL_NUMBER_STRING = "0x0010"; //  43 43 32 36 35 30 20 53 65 6e 73 6f 72 54 61 67 (CC2650 SensorTag)
  public static final String HANDLE_SERIAL_NUMBER_STRING = "0x0012"; // 4e 2e 41 2e (N.A.)
  public static final String HANDLE_FIRMWARE_REVISION_STRING = "0x0014"; // 31 2e 32 30 20 28 4a 75 6c 20 32 30 20 32 30 31 35 29 (1.20 (Jul 20 2015))
  public static final String HANDLE_HARDWARE_REVISION_STRING = "0x0016"; //  50 43 42 20 31 2e 32 2f 31 2e 33 (PCB 1.2/1.3)
  public static final String HANDLE_SOFTWARE_REVISION_STRING = "0x0018"; // 4e 2e 41 2e (N.A.)
  public static final String HANDLE_MANUFACTURER_NAME_STRING = "0x001a"; // 54 65 78 61 73 20 49 6e 73 74 72 75 6d 65 6e 74 73 (Texas Instruments)
  public static final String HANDLE_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = "0x001c"; // FE 00 65 78 70 65 72 69 6d 65 6e 74 61 6c (FE 00 experimental)
  public static final String HANDLE_PNP_ID = "0x001e"; // 01 0d 00 00 00 10 01

  public static final String HANDLE_IR_TEMPERATURE_VALUE = "0x0021";
  public static final String HANDLE_IR_TEMPERATURE_NOTIFICATION = "0x0022"; // write 01:00 to enable, 00:00 to disable
  public static final String HANDLE_IR_TEMPERATURE_ENABLE = "0x0024";
  public static final String HANDLE_IR_TEMPERATURE_PERIOD = "0x0026";

  public static final String HANDLE_HUMIDITY_VALUE = "0x0029";
  public static final String HANDLE_HUMIDITY_NOTIFICATION  = "0x002a";
  public static final String HANDLE_HUMIDITY_ENABLE = "0x002c";
  public static final String HANDLE_HUMIDITY_PERIOD = "0x002e";

  public static final String HANDLE_PRESSURE_VALUE = "0x0031";
  public static final String HANDLE_PRESSURE_NOTIFICATION = "0x0032";
  public static final String HANDLE_PRESSURE_ENABLE = "0x0034";
  public static final String HANDLE_PRESSURE_PERIOD = "0x0036";

  public static final String HANDLE_MOVEMENT_VALUE = "0x0039";
  public static final String HANDLE_MOVEMENT_NOTIFICATION = "0x003a";
  public static final String HANDLE_MOVEMENT_ENABLE = "0x003c";
  public static final String HANDLE_MOVEMENT_PERIOD = "0x003e";

  public static final String VALUE_MOVEMENT_ACTIVATE_ALL_2G = "7f:00";
  public static final String VALUE_MOVEMENT_DEACTIVATE_ALL_2G = "00:00";
  public static final String VALUE_MOVEMENT_ACTIVATE_ALL_4G = "7f:01";
  public static final String VALUE_MOVEMENT_DEACTIVATE_ALL_4G = "00:01";
  public static final String VALUE_MOVEMENT_ACTIVATE_ALL_8G = "7f:02";
  public static final String VALUE_MOVEMENT_DEACTIVATE_ALL_8G = "00:02";
  public static final String VALUE_MOVEMENT_ACTIVATE_ALL_16G = "7f:03";
  public static final String VALUE_MOVEMENT_DEACTIVATE_ALL_16G = "00:03";

  public static final String HANDLE_AMBIENTLIGHT_VALUE = "0x0041";
  public static final String HANDLE_AMBIENTLIGHT_NOTIFICATION = "0x0042";
  public static final String HANDLE_AMBIENTLIGHT_ENABLE = "0x0044";
  public static final String HANDLE_AMBIENTLIGHT_PERIOD = "0x0046";

  public static final String HANDLE_KEYS_STATUS = "0x0049";
  public static final String HANDLE_KEYS_NOTIFICATION = "0x004a";

  public static final String HANDLE_IO_DATA = "0x004e";
  public static final String HANDLE_IO_CONFIG = "0x0050";

  public static final String HANDLE_REGISTER_DATA = "0x0053";
  public static final String HANDLE_REGISTER_NOTIFICATION = "0x0054";
  public static final String HANDLE_REGISTER_ADDRESS = "0x0056";
  public static final String HANDLE_REGISTER_DEVICE_ID = "0x0058";

  public static final String HANDLE_CONNECTION_PARAMETERS = "0x005b";
  public static final String HANDLE_CONNECTION_NOTIFICATION = "0x005c";
  public static final String HANDLE_REQUEST_CONNECTION_PARAMETERS = "0x005e";
  public static final String HANDLE_DISCONNECT_REQUEST = "0x0060";

  public static final String HANDLE_LOAD_IMAGE_IDENTIFY = "0x0063";
  public static final String HANDLE_LOAD_IMAGE_IDENTIFIY_NOTIFICATION = "0x0064";
  public static final String HANDLE_LOAD_IMAGE_BLOCK = "0x0067";
  public static final String HANDLE_LOAD_IMAGE_BLOCK_NOTIFICATION = "0x0068";

  public static final String INTERVAL_IR_TEMPERATURE_300MS_MIN = "1E";
  public static final String INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT = "64";
  public static final String INTERVAL_IR_TEMPERATURE_2550MS_MAX = "FF";

  public static final String INTERVAL_MOVEMENT_300MS_MIN = "0A";
  public static final String INTERVAL_MOVEMENT_1000MS_DEFAULT = "64";
  public static final String INTERVAL_MOVEMENT_2550MS_MAX = "FF";

  public static final String INTERVAL_HUMIDITY_100MS_MIN = "0A";
  public static final String INTERVAL_HUMIDITY_1000MS_DEFAULT = "64";
  public static final String INTERVAL_HUMIDITY_2550MS_MAX = "FF";

  public static final String INTERVAL_PRESSURE_100MS_MIN = "0A";
  public static final String INTERVAL_PRESSURE_1000MS_DEFAULT = "64";
  public static final String INTERVAL_PRESSURE_2550MS_MAX = "FF";

  public static final String INTERVAL_AMBIENTLIGHT_100MS_MIN = "0A";
  public static final String INTERVAL_AMBIENTLIGHT_800MS_DEFAULT = "50";
  public static final String INTERVAL_AMBIENTLIGHT_2550MS_MAX = "FF";


  //@formatter:on
}
