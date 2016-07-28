package de.fhg.fit.biomos.sensorplatform.deprecated;

import de.fhg.fit.biomos.sensorplatform.tools.Gatttool;
import de.fhg.fit.biomos.sensorplatform.tools.GatttoolImpl;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;

public class GatttoolTest {

  public static void main(String[] args) throws InterruptedException {
    Gatttool g = new GatttoolImpl(AddressType.PUBLIC, "00:22:D0:AA:1F:B1");
    new Thread(g).start();

    g.connectBlocking(5);

    System.out.println("BEFORE SLEEP");
    Thread.sleep(30000);
    System.out.println("AFTER SLEEP");

    g.reconnect();

    g.disconnectBlocking();
    g.exitGatttool();

  }

}
