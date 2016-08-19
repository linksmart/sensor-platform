package de.fhg.fit.biomos.sensorplatform.sensorwrapper;

import de.fhg.fit.biomos.sensorplatform.control.HeartRateSampleCollector;
import de.fhg.fit.biomos.sensorplatform.util.AddressType;

/**
 *
 * @author Daniel Pyka
 *
 */
public abstract class AbstractHeartRateSensorWrapper extends AbstractSensorWrapper {

  protected final HeartRateSampleCollector hrsCollector;

  public AbstractHeartRateSensorWrapper(AddressType addressType, String bdAddress, String timestampFormat, HeartRateSampleCollector hrsCollector) {
    super(addressType, bdAddress, timestampFormat);
    this.hrsCollector = hrsCollector;
  }

}
