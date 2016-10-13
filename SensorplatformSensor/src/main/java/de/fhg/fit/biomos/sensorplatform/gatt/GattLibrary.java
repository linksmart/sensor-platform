package de.fhg.fit.biomos.sensorplatform.gatt;

import java.util.UUID;

/**
 * Currently, this interface defines several Bluetooth UUIDs for services and characteristics. This interface may be expanded to define more methods for general
 * information (only device name for now).
 *
 * @author Daniel Pyka
 *
 */
public interface GattLibrary {

  // services
  public final UUID GENERIC_ATTRIBUTE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
  public final UUID GENERIC_ACCESS = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
  public final UUID DEVICE_INFORMATION = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
  public final UUID HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
  public final UUID PULSE_OXIMETER = UUID.fromString("00001822-0000-1000-8000-00805f9b34fb");
  public final UUID BATTERY = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");

  // characteristics
  public final UUID UUID_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_APPEARANCE = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_PERIPHERAL_PRIVACY_FLAG = UUID.fromString("00002a02-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_RECONNECTION_ADDRESS = UUID.fromString("00002a03-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_SERVICE_CHANGED = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_SYSTEM_ID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_MODEL_NUMBER_STRING = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_SERIAL_NUMBER_STRING = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_FIRMWARE_REVISION_STRING = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_HARDWARE_REVISION_STRING = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_SOFTWARE_REVISION_STRING = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_MANUFACTURER_NAME_STRING = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = UUID.fromString("00002a2a-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_PNP_ID = UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb");

  public final UUID UUID_PULSE_OXIMETER = UUID.fromString("00002a5f-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
  public final UUID UUID_BODY_SENSOR_LOCATION = UUID.fromString("00002a38-0000-1000-8000-00805f9b34fb");

  public String getHandleDeviceName();

  public String getHandleBatteryLevel();

}
