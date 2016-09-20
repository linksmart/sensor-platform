package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Texas Instruments SensorTag CC2650<br>
 * This sensor provides measurements for the following parameters: temperature, humidity, ambientlight, pressure, and movement. Press the button on the right
 * side to wake up from standby mode and start advertising itself for 120 seconds. The LED will flash at 1Hz during this time. Afterwards, the SensorTag returns
 * to power saving mode again. The LED does not blink if any device is connected the SensorTag.
 *
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @author Daniel Pyka
 *
 */
public class CC2650lib implements GattLibrary {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS = "A0:E6:F8:B6:37:05";

  // primary services
  public final UUID TEMPERATURE = UUID.fromString("f000aa00-0451-4000-b000-000000000000");
  public final UUID HUMIDITY = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
  public final UUID PRESSURE = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
  public final UUID MOVEMENT = UUID.fromString("f000aa80-0451-4000-b000-000000000000");
  public final UUID AMBIENTLIGHT = UUID.fromString("f000aa70-0451-4000-b000-000000000000");

  public final UUID KEYS_STATUS = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

  public final UUID IO_SERVICE = UUID.fromString("f000aa64-0451-4000-b000-000000000000");
  public final UUID REGISTER_SERVICE = UUID.fromString("f000ac00-0451-4000-b000-000000000000");
  public final UUID UUID_SENSORTAG_CONNECTION_CONTROL = UUID.fromString("f000ccc0-0451-4000-b000-000000000000");
  public final UUID UUID_OVER_THE_AIR_UPDATE = UUID.fromString("f000ffc0-0451-4000-b000-000000000000");

  // characteristics
  public final UUID UUID_IR_TEMPERATURE_VALUE = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
  public final UUID UUID_IR_TEMPERATURE_ENABLE = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
  public final UUID UUID_IR_TEMPERATURE_PERIOD = UUID.fromString("f000aa03-0451-4000-b000-000000000000");

  public final UUID UUID_HUMIDITY_VALUE = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
  public final UUID UUID_HUMIDITY_ENABLE = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
  public final UUID UUID_HUMIDITY_PERIOD = UUID.fromString("f000aa23-0451-4000-b000-000000000000");

  public final UUID UUID_PRESSURE_VALUE = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
  public final UUID UUID_PRESSURE_ENABLE = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
  public final UUID UUID_PRESSURE_PERIOD = UUID.fromString("f000aa44-0451-4000-b000-000000000000");

  public final UUID UUID_MOVEMENT_VALUE = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
  public final UUID UUID_MOVEMENT_ENABLE = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
  public final UUID UUID_MOVEMENT_PERIOD = UUID.fromString("f000aa83-0451-4000-b000-000000000000");

  public final UUID UUID_AMBIENTLIGHT_VALUE = UUID.fromString("f000aa71-0451-4000-b000-000000000000");
  public final UUID UUID_AMBIENTLIGHT_ENABLE = UUID.fromString("f000aa72-0451-4000-b000-000000000000");
  public final UUID UUID_AMBIENTLIGHT_PERIOD = UUID.fromString("f000aa73-0451-4000-b000-000000000000");

  public final UUID UUID_KEYS_STATUS = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

  public final UUID UUID_IO_DATA = UUID.fromString("f000aa65-0451-4000-b000-000000000000");
  public final UUID UUID_IO_CONFIG = UUID.fromString("f000aa66-0451-4000-b000-000000000000");

  public final UUID UUID_REGISTER_DATA = UUID.fromString("f000ac01-0451-4000-b000-000000000000");
  public final UUID UUID_REGISTER_ADDRESS = UUID.fromString("f000ac02-0451-4000-b000-000000000000");
  public final UUID UUID_REGISTER_DEVICE_ID = UUID.fromString("f000ac03-0451-4000-b000-000000000000");

  public final UUID UUID_CONNECTION_PARAMETERS = UUID.fromString("f000ccc1-0451-4000-b000-000000000000");
  public final UUID UUID_REQUEST_CONNECTION_PARAMETERS = UUID.fromString("f000ccc2-0451-4000-b000-000000000000");
  public final UUID UUID_DISCONNECT_REQUEST = UUID.fromString("f000ccc3-0451-4000-b000-000000000000");

  public final UUID UUID_OAD_IMAGE_IDENTIFY = UUID.fromString("f000ffc1-0451-4000-b000-000000000000");
  public final UUID UUID_OAD_IMAGE_BLOCK = UUID.fromString("f000ffc2-0451-4000-b000-000000000000");

  public final String HANDLE_DEVICE_NAME = "0x0003";
  public final String HANDLE_APPEARANCE = "0x0005";
  public final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x0007";

  public final String HANDLE_SERVICE_CHANGED = "0x000a";

  public final String HANDLE_SYSTEM_ID = "0x000e";
  public final String HANDLE_MODEL_NUMBER_STRING = "0x0010";
  public final String HANDLE_SERIAL_NUMBER_STRING = "0x0012";
  public final String HANDLE_FIRMWARE_REVISION_STRING = "0x0014";
  public final String HANDLE_HARDWARE_REVISION_STRING = "0x0016";
  public final String HANDLE_SOFTWARE_REVISION_STRING = "0x0018";
  public final String HANDLE_MANUFACTURER_NAME_STRING = "0x001a";
  public final String HANDLE_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = "0x001c";
  public final String HANDLE_PNP_ID = "0x001e";

  public final String HANDLE_IR_TEMPERATURE_VALUE = "0x0021";
  public final String HANDLE_IR_TEMPERATURE_NOTIFICATION = "0x0022";
  public final String HANDLE_IR_TEMPERATURE_ENABLE = "0x0024";
  public final String HANDLE_IR_TEMPERATURE_PERIOD = "0x0026";

  public final String HANDLE_HUMIDITY_VALUE = "0x0029";
  public final String HANDLE_HUMIDITY_NOTIFICATION  = "0x002a";
  public final String HANDLE_HUMIDITY_ENABLE = "0x002c";
  public final String HANDLE_HUMIDITY_PERIOD = "0x002e";

  public final String HANDLE_PRESSURE_VALUE = "0x0031";
  public final String HANDLE_PRESSURE_NOTIFICATION = "0x0032";
  public final String HANDLE_PRESSURE_ENABLE = "0x0034";
  public final String HANDLE_PRESSURE_PERIOD = "0x0036";

  public final String HANDLE_MOVEMENT_VALUE = "0x0039";
  public final String HANDLE_MOVEMENT_NOTIFICATION = "0x003a";
  public final String HANDLE_MOVEMENT_ENABLE = "0x003c";
  public final String HANDLE_MOVEMENT_PERIOD = "0x003e";

  public final String VALUE_MOVEMENT_ACTIVATE_ALL_2G = "7f:00";
  public final String VALUE_MOVEMENT_DEACTIVATE_ALL_2G = "00:00";
  public final String VALUE_MOVEMENT_ACTIVATE_ALL_4G = "7f:01";
  public final String VALUE_MOVEMENT_DEACTIVATE_ALL_4G = "00:01";
  public final String VALUE_MOVEMENT_ACTIVATE_ALL_8G = "7f:02";
  public final String VALUE_MOVEMENT_DEACTIVATE_ALL_8G = "00:02";
  public final String VALUE_MOVEMENT_ACTIVATE_ALL_16G = "7f:03";
  public final String VALUE_MOVEMENT_DEACTIVATE_ALL_16G = "00:03";

  public final String HANDLE_AMBIENTLIGHT_VALUE = "0x0041";
  public final String HANDLE_AMBIENTLIGHT_NOTIFICATION = "0x0042";
  public final String HANDLE_AMBIENTLIGHT_ENABLE = "0x0044";
  public final String HANDLE_AMBIENTLIGHT_PERIOD = "0x0046";

  public final String HANDLE_KEYS_STATUS = "0x0049";
  public final String HANDLE_KEYS_NOTIFICATION = "0x004a";

  public final String HANDLE_IO_DATA = "0x004e";
  public final String HANDLE_IO_CONFIG = "0x0050";

  public final String HANDLE_REGISTER_DATA = "0x0053";
  public final String HANDLE_REGISTER_NOTIFICATION = "0x0054";
  public final String HANDLE_REGISTER_ADDRESS = "0x0056";
  public final String HANDLE_REGISTER_DEVICE_ID = "0x0058";

  public final String HANDLE_CONNECTION_PARAMETERS = "0x005b";
  public final String HANDLE_CONNECTION_NOTIFICATION = "0x005c";
  public final String HANDLE_REQUEST_CONNECTION_PARAMETERS = "0x005e";
  public final String HANDLE_DISCONNECT_REQUEST = "0x0060";

  public final String HANDLE_LOAD_IMAGE_IDENTIFY = "0x0063";
  public final String HANDLE_LOAD_IMAGE_IDENTIFIY_NOTIFICATION = "0x0064";
  public final String HANDLE_LOAD_IMAGE_BLOCK = "0x0067";
  public final String HANDLE_LOAD_IMAGE_BLOCK_NOTIFICATION = "0x0068";

  public final String ENABLE_MEASUREMENT = "01";
  public final String DISABLE_MEASUREMENT = "00";

  public final String INTERVAL_IR_TEMPERATURE_300MS_MIN = "1E";
  public final String INTERVAL_IR_TEMPERATURE_1000MS_DEFAULT = "64";
  public final String INTERVAL_IR_TEMPERATURE_2550MS_MAX = "FF";

  public final String INTERVAL_MOVEMENT_300MS_MIN = "0A";
  public final String INTERVAL_MOVEMENT_1000MS_DEFAULT = "64";
  public final String INTERVAL_MOVEMENT_2550MS_MAX = "FF";

  public final String INTERVAL_HUMIDITY_100MS_MIN = "0A";
  public final String INTERVAL_HUMIDITY_1000MS_DEFAULT = "64";
  public final String INTERVAL_HUMIDITY_2550MS_MAX = "FF";

  public final String INTERVAL_PRESSURE_100MS_MIN = "0A";
  public final String INTERVAL_PRESSURE_1000MS_DEFAULT = "64";
  public final String INTERVAL_PRESSURE_2550MS_MAX = "FF";

  public final String INTERVAL_AMBIENTLIGHT_100MS_MIN = "0A";
  public final String INTERVAL_AMBIENTLIGHT_800MS_DEFAULT = "50";
  public final String INTERVAL_AMBIENTLIGHT_2550MS_MAX = "FF";

  //@formatter:on

  public CC2650lib() {
  }

  @Override
  public String getHandleDeviceName() {
    return this.HANDLE_DEVICE_NAME;
  }

}
