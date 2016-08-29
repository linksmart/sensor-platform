package de.fhg.fit.biomos.sensorplatform.gatt;

/**
 *
 * @author Daniel Pyka
 *
 */
public class BLE113lib implements PulseOximeterGattLibrary {
  //@formatter:off

  public static final String DEFAULT_BDADDRESS_ADIDAS = "00:07:80:6A:F2:51";

  // characteristics
  public final String HANDLE_DEVICE_NAME = "0x0003";

  public final String HANDLE_APPEARANCE = "0x0006";

  public final String HANDLE_MANUFACTURER_NAME_STRING = "0x000a";

  public final String HANDLE_MODEL_NUMBER_STRING = "0x000d";

  public final String HANDLE_PULSE_OXIMETER_MEASUREMENT = "0x0011";
  public final String HANDLE_PULSE_OXIMETER_NOTIFICATION = "0x0012";

  public final String HANDLE_PULSE_OXIMETER_FEATURES = "0x0015";

  //@formatter:on

  public BLE113lib() {
  }

  @Override
  public String getHandleDeviceName() {
    return this.HANDLE_DEVICE_NAME;
  }

  @Override
  public String getHandlePulseOximeterMeasurement() {
    return this.HANDLE_PULSE_OXIMETER_MEASUREMENT;
  }

  @Override
  public String getHandlePulseOximeterNotification() {
    return this.HANDLE_PULSE_OXIMETER_NOTIFICATION;
  }
}
