package de.fhg.fit.biomos.sensorplatform.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;

/**
 * This will simulate a gatttool with Polar H7 for running the Sensorplatform application on a Windows machine. For testing purposes only!!
 *
 * @author Daniel Pyka
 *
 */
public class VirtualGatttoolPolarH7 extends Gatttool {

  private static final Logger LOG = LoggerFactory.getLogger(VirtualGatttoolPolarH7.class);

  private final State state;

  private final byte[] buffer = new byte[1024];
  private BufferedWriter streamToSensor = null;
  private BufferedReader streamFromSensor = null;

  public VirtualGatttoolPolarH7(String bdAddress, AddressType addressType, SecurityLevel secLevel) {
    super(bdAddress, addressType, secLevel);
    this.state = State.DISCONNECTED;
    try {
      this.streamToSensor = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
      this.streamFromSensor = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.buffer)));
      LOG.info("gatttool process for " + this.bdAddress + " created");
      LOG.info("address type is " + this.addressType);
      this.streamToSensor.write(CMD_SEC_LEVEL + " " + this.secLevel);
      this.streamToSensor.newLine();
      this.streamToSensor.flush();
      LOG.info("security level set to " + this.secLevel);
    } catch (IOException e) {
      LOG.error("creating gatttool process failed", e);
    }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }

}
