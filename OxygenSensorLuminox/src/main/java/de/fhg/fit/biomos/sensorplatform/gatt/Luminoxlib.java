package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Texas Instruments SensorTag CC2650<br>
 * This sensor provides measurements for the following parameters: temperature, humidity, ambientlight, pressure, and movement. By pressing the button on the
 * right, the sensor will advertise itself for 120 seconds and it will return to sleep mode afterwards. The LED will flash at 1Hz during this time. Afterwards,
 * the SensorTag returns to power saving mode again. The LED does not blink if a device is connected to the sensor.
 *
 * @see <a href="http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide">CC2650 SensorTag User's Guide</a>
 *
 * @author Daniel Pyka
 *
 */
public class Luminoxlib implements GattLibrary {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS = "00:07:80:6a:f2:51";

  // primary services
  public final UUID O2TP_VALUES = UUID.fromString("00431c4a-a7a4-428b-a96d-d92d43c8c7cf");

  public final String HANDLE_DEVICE_NAME = "0x0003";
  public final String HANDLE_APPEARANCE = "0x0005";

  public final String HANDLE_MANUFACTURER_NAME_STRING = "0x0008";
  public final String HANDLE_MANUFACTURER_MODELNUMBER = "0x000A";
  public final String HANDLE_MANUFACTURER_BLE_NAME_STRING = "0x000C";

  public final String HANDLE_BATTERY_STATUS_VALUE = "0x0010";
  public final String HANDLE_BATTERY_STATUS_NOTIFICATION = "0x0011";

  public final String HANDLE_OXYGEN_VALUE = "0x0014";
  public final String HANDLE_OXYGEN_NOTIFICATION = "0x0015";


  //RS232 Command Set for Luminox

  public final String OUTPUT_MODE = "M";
  public final String REQUEST_PPO2_VALUE = "O";
  public final String REQUEST_O2_VALUE = "%";
  public final String REQUEST_TEMPERATURE_VALUE = "T";
  public final String REQUEST_PRESSURE_VALUE = "p";
  public final String REQUEST_SENSOR_STATUS = "e";
  public final String REQUEST_ALL_VALUES = "A";
  public final String REQUEST_SENSOR_INFORMATION = "#";

  public final String REQUEST_ARGUMENT_0 = "0";
  public final String REQUEST_ARGUMENT_1 = "1";
  public final String REQUEST_ARGUMENT_2 = "2";
  public final String REQUEST_ARGUMENT_3 = "3";
  public final String REQUEST_ARGUMENT_4 = "4";
  public final String REQUEST_ARGUMENT_5 = "5";
  public final String REQUEST_ARGUMENT_6 = "6";
  public final String REQUEST_ARGUMENT_7 = "7";
  public final String REQUEST_ARGUMENT_8 = "8";
  public final String REQUEST_ARGUMENT_9 = "9";

  public final String REQUEST_SEPARATOR = " ";
  public final String REQUEST_TERMINATOR = "\r\n";

  //@formatter:on

  public Luminoxlib() {
  }

  @Override
  public String getHandleDeviceName() {
    return this.HANDLE_DEVICE_NAME;
  }

  @Override
  public String getHandleBatteryLevel() {
    return this.HANDLE_BATTERY_STATUS_VALUE;
  }

}
