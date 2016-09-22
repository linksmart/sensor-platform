package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
@Deprecated
public class SecurityManager {

  private static final Logger LOG = LoggerFactory.getLogger(SecurityManager.class);

  private static final File BLUEZ_STORAGE = new File("/var/lib/bluetooth");
  private static final String BLUEZ_INFO_FILENAME = "info";
  // file structure:
  // /var/lib/bluetooth/<LocalBluetoothAddress>/<Device_1_BluetoothAddress>/info
  // /var/lib/bluetooth/<LocalBluetoothAddress>/<Device_2_BluetoothAddress>/info

  private static final Pattern PATTERN_DEVICENAME = Pattern.compile("Name=(.*)");
  private static final Pattern PATTERN_ADDRESSTYPE = Pattern.compile("AddressType=(.*)");

  private static final String PAIRING_IDENTIFIER_1 = "[IdentityResolvingKey]";
  private static final String PAIRING_IDENTIFIER_2 = "[RemoteSignatureKey]";
  private static final String PAIRING_IDENTIFIER_3 = "[LocalSignatureKey]";
  private static final String PAIRING_IDENTIFIER_4 = "[LongTermKey]";
  private static final String PAIRING_IDENTIFIER_5 = "[SlaveLongTermKey]";

  private static final String AUTH_IDENTIFIER_1 = "Authenticated=true";
  private static final String AUTH_IDENTIFIER_2 = "Authenticated=1";

  private final List<BluetoothDevice> knownDevices = new ArrayList<BluetoothDevice>();

  private final Hciconfig hciconfig;

  @Inject
  public SecurityManager() {
    this.hciconfig = new HciconfigImpl();
  }

  public void loadStoredDevices() {
    File bluezStore;
    try {
      bluezStore = new File(BLUEZ_STORAGE, this.hciconfig.getLocalBDaddress());
    } catch (NullPointerException e) {
      LOG.error("no local bd address", e);
      return;
    }
    if (bluezStore.exists() && bluezStore.isDirectory()) {
      File[] directories = bluezStore.listFiles(File::isDirectory);
      for (File deviceFolder : directories) {
        String deviceAddress = deviceFolder.getName();
        if (deviceAddress.equals("cache")) {
          continue;
        }
        try {
          this.knownDevices.add(parse(getInfoFilePath(deviceAddress), deviceAddress));
        } catch (IOException e) {
          LOG.error("error parsing file", e);
        }
      }
    } else {
      LOG.info("no stored bluetooth devices");
    }
  }

  private Path getInfoFilePath(String deviceAddress) {
    return new File(new File(new File(BLUEZ_STORAGE, this.hciconfig.getLocalBDaddress()), deviceAddress), BLUEZ_INFO_FILENAME).toPath();
  }

  private BluetoothDevice parse(Path storedDevice, String deviceAddress) throws IOException {
    String content = new String(Files.readAllBytes(storedDevice));
    String name = null;
    String bdaddress = deviceAddress;
    AddressType addressType = null;
    boolean hasIdentityResolvingKey = false;
    boolean hasRemoteSignatureKey = false;
    boolean hasLocalSignatureKey = false;
    boolean hasLongTermKey = false;
    boolean hasSlaveLongTermKey = false;

    boolean authenticated = false;

    Matcher m = PATTERN_DEVICENAME.matcher(content);
    if (m.find()) {
      name = m.group(1);
    }

    m = PATTERN_ADDRESSTYPE.matcher(content);
    if (m.find()) {
      addressType = AddressType.valueOf(m.group(1).toUpperCase());
    }

    if (content.contains(PAIRING_IDENTIFIER_1)) {
      hasIdentityResolvingKey = true;
    }
    if (content.contains(PAIRING_IDENTIFIER_2)) {
      hasRemoteSignatureKey = true;
    }
    if (content.contains(PAIRING_IDENTIFIER_3)) {
      hasLocalSignatureKey = true;
    }
    if (content.contains(PAIRING_IDENTIFIER_4)) {
      hasLongTermKey = true;
    }
    if (content.contains(PAIRING_IDENTIFIER_5)) {
      hasSlaveLongTermKey = true;
    }
    boolean paired = hasIdentityResolvingKey && hasRemoteSignatureKey && hasLocalSignatureKey && hasLongTermKey && hasSlaveLongTermKey;

    if (content.contains(AUTH_IDENTIFIER_1) && content.contains(AUTH_IDENTIFIER_2)) {
      authenticated = true;
    }

    SecurityLevel secLevel;
    if (paired) {
      if (authenticated) {
        secLevel = SecurityLevel.HIGH;
      } else {
        secLevel = SecurityLevel.MEDIUM;
      }
    } else {
      secLevel = SecurityLevel.LOW;
    }

    BluetoothDevice dev = new BluetoothDevice(name, bdaddress, addressType, secLevel);
    return dev;
  }

  /**
   * Do not use!!!
   * 
   * @param asw
   * @return
   * @throws IOException
   */
  @Deprecated
  public AbstractSensorWrapper<?> pairDevice(AbstractSensorWrapper<?> asw) throws IOException {
    for (BluetoothDevice btDev : this.knownDevices) {
      if (btDev.getBDAddress().equals(asw.getSensor().getBDaddress())) {
        LOG.info("device " + asw.getSensor().getBDaddress() + " is known, security level " + btDev.getSecLevel());
        LOG.info("pairing (if possible) already done");
        return asw;
      }
    }
    LOG.info("device " + asw.getSensor().getBDaddress() + " is unknown");
    String bdAddress = asw.getSensor().getBDaddress();
    // Hcitool hcitool = new HcitoolImpl(5);
    // for (DetectedDevice device : hcitool.getDetectedDevices()) {
    // if (device.getBdAddress().equals(bdAddress)) {
    // hcitool.pair(bdAddress);
    // BluetoothDevice btDev = parse(getInfoFilePath(asw.getSensor().getBDaddress()), asw.getSensor().getBDaddress());
    // this.knownDevices.add(btDev);
    // return asw;
    // }
    // }
    // hcitool.scan();
    // hcitool.pair(bdAddress);

    BluetoothDevice btDev = parse(getInfoFilePath(asw.getSensor().getBDaddress()), asw.getSensor().getBDaddress());
    this.knownDevices.add(btDev);
    LOG.info("device is now \"known\"");
    return asw;
  }

}
