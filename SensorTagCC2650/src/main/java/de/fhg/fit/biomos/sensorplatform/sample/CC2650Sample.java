package de.fhg.fit.biomos.sensorplatform.sample;

import java.util.Arrays;

/**
 *
 * @author Daniel Pyka
 *
 */
public class CC2650Sample {

  private class Measurement {
  };

  private class TemperatureMeasurement extends Measurement {

    private final String UNIT_DEGREES_CELSIUS = "°C";
    private final float objectTemperature;
    private final float dieTemperature;

    public TemperatureMeasurement(float objectTemperature, float dieTemperature) {
      this.objectTemperature = objectTemperature;
      this.dieTemperature = dieTemperature;
    }

    @Override
    public String toString() {
      return "{\"temperature\":{\"objectTemperature\":{\"value\":" + this.objectTemperature + ",\"unit\":\"" + this.UNIT_DEGREES_CELSIUS + "\"}"
          + ",\"dieTemperature\":{\"value\":" + this.dieTemperature + ",\"unit\":\"" + this.UNIT_DEGREES_CELSIUS + "\"}}}";
    }
  }

  private class HumidityMeasurement extends Measurement {

    private final String UNIT_DEGREES_CELSIUS = "°C";
    private final String UNIT_RELATIVE_HUMIDITY = "%RH";
    private final float temperature;
    private final float humidity;

    public HumidityMeasurement(float temperature, float humidity) {
      this.temperature = temperature;
      this.humidity = humidity;
    }

    @Override
    public String toString() {
      return "{\"humidity\":{\"temperature\":{\"value\":" + this.temperature + ",\"unit\":\"" + this.UNIT_DEGREES_CELSIUS + "\"}" + ",\"humidity\":{\"value\":"
          + this.humidity + ",\"unit\":\"" + this.UNIT_RELATIVE_HUMIDITY + "\"}}}";
    }
  }

  private class AmbientlightMeasurement extends Measurement {

    private final String UNIT_LUX = "lx";
    private final float ambientlight;

    public AmbientlightMeasurement(float ambientlight) {
      this.ambientlight = ambientlight;
    }

    @Override
    public String toString() {
      return "{\"ambientlight\":{\"ambientlight\":{\"value\":" + this.ambientlight + ",\"unit\":\"" + this.UNIT_LUX + "\"}}}";
    }
  }

  private class PressureMeasurement extends Measurement {

    private final String UNIT_DEGREES_CELSIUS = "°C";
    private final String UNIT_HECTOPASCAL = "hPa";
    private final float temperature;
    private final float pressure;

    public PressureMeasurement(float temperature, float pressure) {
      this.temperature = temperature;
      this.pressure = pressure;
    }

    @Override
    public String toString() {
      return "{\"pressure\":{\"temperature\":{\"value\":" + this.temperature + ",\"unit\":\"" + this.UNIT_DEGREES_CELSIUS + "\"}" + ",\"pressure\":{\"value\":"
          + this.pressure + ",\"unit\":\"" + this.UNIT_HECTOPASCAL + "\"}}}";
    }
  }

  private class MovementMeasurement extends Measurement {

    private final String UNIT_DEGREES_PER_SECOND = "deg/s";
    private final String UNIT_GRAVITY = "G";
    private final String UNIT_MICROTESLA = "uT";
    private final float[] rotation_XYZ;
    private final float[] acceleration_XYZ;
    private final float[] magnetism_XYZ;

    public MovementMeasurement(float[] rotation_XYZ, float[] acceleration_XYZ, float[] magnetism_XYZ) {
      this.rotation_XYZ = rotation_XYZ;
      this.acceleration_XYZ = acceleration_XYZ;
      this.magnetism_XYZ = magnetism_XYZ;
    }

    @Override
    public String toString() {
      return "{\"movement\":{\"rotation\":{\"value\":" + Arrays.toString(this.rotation_XYZ) + ",\"unit\":\"" + this.UNIT_DEGREES_PER_SECOND + "\"}"
          + ",\"acceleration\":{\"value\":" + Arrays.toString(this.acceleration_XYZ) + ",\"unit\":\"" + this.UNIT_GRAVITY + "\"},\"magnetism\":{\"value\":"
          + Arrays.toString(this.magnetism_XYZ) + ",\"unit\":\"" + this.UNIT_MICROTESLA + "\"}}}";
    }
  }

  private final String timestamp;
  private final String bdAddress;
  private Measurement measurement;

  public CC2650Sample(String timestamp, String bdAddress) {
    this.timestamp = timestamp;
    this.bdAddress = bdAddress;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public String getBDaddress() {
    return this.bdAddress;
  }

  public Measurement getSample() {
    return this.measurement;
  }

  public void setTemperatureMeasurement(float objectTemperature, float dieTemperature) {
    this.measurement = new TemperatureMeasurement(objectTemperature, dieTemperature);
  }

  public void setHumidityMeasurement(float temperature, float humidity) {
    this.measurement = new HumidityMeasurement(temperature, humidity);
  }

  public void setAmbientlightMeasurement(float objectTemperature) {
    this.measurement = new AmbientlightMeasurement(objectTemperature);
  }

  public void setPressureMeasurement(float temperature, float pressure) {
    this.measurement = new PressureMeasurement(temperature, pressure);
  }

  public void setMovementMeasurement(float[] rotation_XYZ, float[] acceleration_XYZ, float[] magnetism_XYZ) {
    this.measurement = new MovementMeasurement(rotation_XYZ, acceleration_XYZ, magnetism_XYZ);
  }

  @Override
  public String toString() {
    return "{\"timestamp\":\" " + this.timestamp + "\",\"device\":\"" + this.bdAddress + ",\"value\":" + this.measurement.toString() + "}";
  }

}
