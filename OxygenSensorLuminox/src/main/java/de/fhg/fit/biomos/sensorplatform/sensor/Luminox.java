package de.fhg.fit.biomos.sensorplatform.sensor;

import java.io.BufferedWriter;

import de.fhg.fit.biomos.sensorplatform.gatt.Luminoxlib;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.util.AddressType;
import de.fhg.fit.biomos.sensorplatform.util.SecurityLevel;
import de.fhg.fit.biomos.sensorplatform.util.SensorName;

/**
 *
 *
 * @author Daniel Pyka
 *
 */
public class Luminox extends AbstractSensor<Luminoxlib> {

  private static final Logger log = LoggerFactory.getLogger(Luminox.class);




  public Luminox(SensorName name, String bdaddress, JSONObject settings) {
    super(new Luminoxlib(), name, bdaddress, AddressType.PUBLIC, SecurityLevel.LOW, settings);
  }


  @Override
  public void enableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String enableNotification) {

  }

  @Override
  public void disableDataNotification(BufferedWriter streamToSensor, String charWriteCmd, String disableNotification) {

  }
}
