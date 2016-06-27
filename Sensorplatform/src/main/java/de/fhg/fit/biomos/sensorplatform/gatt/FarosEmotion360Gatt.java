package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Three channel heart rate sensor. Instead of the standard Bluetooth specification, it uses BlueRadios serial port (BRSP) which is complicated to implement.
 * General data (e.g. device name) is available through standard specification. In the resource folder there is an example program for android, how to use BRSP.
 * Due to heavy reliance on the Android framework, this solution is not easily applicable to the Sensorplatform.
 *
 * @author Daniel Pyka
 *
 */
public abstract class FarosEmotion360Gatt {
  //@formatter:off

  // EC:FE:7E:15:D5:EB

  // cmd: primary
  public static final UUID GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public static final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public static final UUID BATTERY_SERVICE = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
  public static final UUID BLUERADIOS_SERIAL_PORT = UUID.fromString("da2b84f1-6279-48de-bdc0-afbea0226079"); // BRSP

  // characteristics
  public static final UUID UUID_DEVICENAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public static final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
  public static final UUID UUID_BATTERY_PERCENT = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");

  public static final UUID UUID_BRSP_INFO = UUID.fromString("99564a02-dc01-4d3c-b04e-3bb1ef0571b2");
  public static final UUID UUID_BRSP_MODE = UUID.fromString("a87988b9-694c-479c-900e-95dfa6c00a24");
  public static final UUID UUID_BRSP_RX = UUID.fromString("bf03260c-7205-4c25-af43-93b1c299d159");
  public static final UUID UUID_BRSP_TX = UUID.fromString("18cda784-4bd3-4370-85bb-bfed91ec86af");
  public static final UUID UUID_BRSP_CTS = UUID.fromString("0a1934f5-24b8-4f13-9842-37bb167c6aff");
  public static final UUID UUID_BRSP_RTS = UUID.fromString("fdd6b4d3-046d-4330-bdec-1fd0c90cb43b");

  public static final String HANDLE_DEVICE_NAME = "0x004"; // ^= FAROS-1620509
  public static final String HANDLE_APPEARANCE = "0x006"; // 00 00
  public static final String HANDLE_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "0x008"; // 08 00 10 00 00 00 90 01
  public static final String HANDLE_BATTERY_PERCENT = "0x000b"; // 64 ^= 100%

  public static final String HANDLE_BRSP_INFO = "0x000f"; // 03 00 02 11 00 13 00 15 00 16 00 18 00 1a 00 1b 00
  public static final String HANDLE_BRSP_MODE = "0x0011"; // 00
  public static final String HANDLE_BRSP_RX = "0x0013"; // write only!
  public static final String HANDLE_BRSP_TX = "0x0015"; // no read/ Data sent to Client PC/ANDROID from BLE (?????)
  public static final String HANDLE_BRSP_CTS = "0x0018"; // 00
  public static final String HANDLE_BRSP_RTS = "0x001a"; // 00

  //@formatter:on
}
