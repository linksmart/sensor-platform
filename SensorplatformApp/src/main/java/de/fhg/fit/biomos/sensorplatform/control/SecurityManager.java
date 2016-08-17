package de.fhg.fit.biomos.sensorplatform.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.tools.BluetoothctlImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.BluetoothDevice;

/**
 *
 * @author Daniel Pyka
 *
 */
public class SecurityManager {

  private static final Logger LOG = LoggerFactory.getLogger(SecurityManager.class);

  private static final Pattern PATTERN_DEVICENAME = Pattern.compile("Name=(.*)");
  private static final Pattern PATTERN_ADDRESSTYPE = Pattern.compile("AddressType=(.*)");

  private static final String PAIRING_IDENTIFIER_1 = "[IdentityResolvingKey]";
  private static final String PAIRING_IDENTIFIER_2 = "[RemoteSignatureKey]";
  private static final String PAIRING_IDENTIFIER_3 = "[LocalSignatureKey]";
  private static final String PAIRING_IDENTIFIER_4 = "[LongTermKey]";
  private static final String PAIRING_IDENTIFIER_5 = "[SlaveLongTermKey]";

  private final BluetoothctlImpl btctl;

  private final List<BluetoothDevice> btDevices = new ArrayList<BluetoothDevice>();

  @Inject
  public SecurityManager() {
    this.btctl = new BluetoothctlImpl();
  }

  public void loadStoredDevices() {
    String localController = this.btctl.getLocalControllerAddress();
    if (!localController.equals("unknown")) {
      File bluezStore = new File(new File("/var/lib/bluetooth"), localController);
      if (bluezStore.exists()) {
        File[] directories = bluezStore.listFiles(File::isDirectory);
        for (File storedDevice : directories) {
          if (storedDevice.getName().equals("cache")) {
            continue;
          }
          LOG.info(storedDevice.toString());
          try {
            this.btDevices.add(parse(storedDevice));
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

  public BluetoothDevice parse(File peripheral) throws IOException {
    LOG.info("checking " + peripheral);
    String content = new String(Files.readAllBytes(new File(peripheral, "info").toPath()));
    String name = null;
    String bdaddress = peripheral.getName();
    AddressType addressType = null;
    boolean hasIdentityResolvingKey = false;
    boolean hasRemoteSignatureKey = false;
    boolean hasLocalSignatureKey = false;
    boolean hasLongTermKey = false;
    boolean hasSlaveLongTermKey = false;

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

    BluetoothDevice dev = new BluetoothDevice(name, bdaddress, addressType);
    dev.setPaired(hasIdentityResolvingKey && hasRemoteSignatureKey && hasLocalSignatureKey && hasLongTermKey && hasSlaveLongTermKey);
    return dev;
  }

  public void pairDevice(String bdAddress) {

  }

}
