package de.fhg.fit.biomos.sensorplatform.control;

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

import de.fhg.fit.biomos.sensorplatform.sensorwrapper.AbstractSensorWrapper;
import de.fhg.fit.biomos.sensorplatform.tools.Hciconfig;
import de.fhg.fit.biomos.sensorplatform.tools.HciconfigImpl;
import de.fhg.fit.biomos.sensorplatform.tools.Hcitool;
import de.fhg.fit.biomos.sensorplatform.tools.HcitoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothDevice;
import de.fhg.fit.biomos.sensorplatform.util.GatttoolSecurityLevel;

/**
 *
 * @author Daniel Pyka
 *
 */
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

  private final Hcitool hcitool;
  private final Hciconfig hciconfig;

  public SecurityManager() {
    this.hcitool = new HcitoolImpl();
    this.hciconfig = new HciconfigImpl();
  }

  public List<BluetoothDevice> getBluetoothDevices() {
    return this.knownDevices;
  }

  public void unblockController() {
    this.hciconfig.down();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep failed");
    }
    this.hciconfig.up();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      LOG.error("sleep failed");
    }
  }

  public void loadStoredDevices() {
    if (this.hciconfig.getLocalBDaddress() != null) {
      File bluezStore = new File(BLUEZ_STORAGE, this.hciconfig.getLocalBDaddress());
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
    } else {
      LOG.error("failed to obtain local bluetooth mac address");
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

    GatttoolSecurityLevel secLevel;
    if (paired) {
      if (authenticated) {
        secLevel = GatttoolSecurityLevel.HIGH;
      } else {
        secLevel = GatttoolSecurityLevel.MEDIUM;
      }
    } else {
      secLevel = GatttoolSecurityLevel.LOW;
    }

    BluetoothDevice dev = new BluetoothDevice(name, bdaddress, addressType, secLevel);
    return dev;
  }

  public GatttoolSecurityLevel pairDevice(AbstractSensorWrapper asw) {
    for (BluetoothDevice btDev : this.knownDevices) {
      if (btDev.getBdAddress().equals(asw.getBDaddress())) {
        LOG.info("device " + asw.getBDaddress() + " is known, security level" + btDev.getSecLevel());
        LOG.info("pairing (if possible) already done");
        return btDev.getSecLevel();
      }
    }
    LOG.info("device " + asw.getBDaddress() + " is unknown");
    if (!this.hcitool.getFoundDevices().contains(asw.getBDaddress())) {
      LOG.info("device not yet detected - try scanning (blocking)");
      this.hcitool.scan();
    }
    this.hcitool.pair(asw.getBDaddress());
    try {
      BluetoothDevice btDevice = parse(getInfoFilePath(asw.getBDaddress()), asw.getBDaddress());
      this.knownDevices.add(btDevice);
      LOG.info("device is now \"known\"");
      return btDevice.getSecLevel();
    } catch (IOException e) {
      LOG.error("bad info file - will try to continue with security level low", e);
    }
    return GatttoolSecurityLevel.LOW;
  }

}
