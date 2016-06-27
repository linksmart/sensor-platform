package de.fhg.fit.biomos.sensorplatform.bluetooth;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class Permissions {

  // characteristics properties
  public static final String GENERIC_PERMISSION_BROADCAST = "0x01";
  public static final String GENERIC_PERMISSION_READ = "0x02";
  public static final String GENERIC_PERMISSION_WRITE_WITHOUT_RESPONSE = "0x04";
  public static final String GENERIC_PERMISSION_WRITE = "0x08";
  public static final String GENERIC_PERMISSION_NOTIFY = "0x10";
  public static final String GENERIC_PERMISSION_INDICATE = "0x20";
  public static final String GENERIC_PERMISSION_AUTHENTICATED_SIGNED_WRITES = "0x40";
  public static final String GENERIC_PERMISSION_EXTENDED_PROPERTIES = "0x80";

  public static final String FAROS_PERMISSION_UKNOWN_1 = "0x0a";
  public static final String FAROS_PERMISSION_UKNOWN_2 = "0x30";
  public static final String FAROS_PERMISSION_UKNOWN_3 = "0x0e";
  public static final String FAROS_PERMISSION_UKNOWN_4 = "0x32";

  public static final String POLAR_PERMISSION_UKNOWN_1 = "0x28";
}
