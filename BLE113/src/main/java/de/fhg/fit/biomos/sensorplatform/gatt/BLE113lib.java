package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class BLE113lib {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS_ADIDAS = "00:07:80:6A:F2:51";

  // characteristics
  public static final UUID UUID_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_DEVICE_NAME = "0x0003";

  public static final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_APPEARANCE = "0x0006";

  public static final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MANUFACTURER_NAME_STRING = "0x000a";

  public static final UUID UUID_MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_MODEL_NUMBER_STRING = "0x000d";

  public static final UUID UUID_PULSE_OXIMETER = UUID.fromString("00002a5f-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PULSE_OXIMETER_MEASUREMENT = "0x0011";
  public static final String HANDLE_PULSE_OXIMETER_NOTIFICATION = "0x0012";

  public static final UUID UUID_PULSE_OXIMETER_FEATURES = UUID.fromString("00002a60-0000-1000-8000-00805f9b34fb");
  public static final String HANDLE_PULSE_OXIMETER_FEATURES = "0x0015";


  //@formatter:on
}
