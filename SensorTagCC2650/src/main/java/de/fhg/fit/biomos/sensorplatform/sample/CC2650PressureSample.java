package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for pressure samples.
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "CC2650PressureSample")
public class CC2650PressureSample implements Serializable {

  private static final long serialVersionUID = 6429269084094852998L;

  private static final String UNIT_DEGREES_CELSIUS = "°C";
  private static final String UNIT_HECTOPASCAL = "hPa";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "timestamp")
  private String timestamp;
  @Column(name = "device")
  private String bdAddress;
  @Column(name = "temperature")
  private Float temperature;
  @Column(name = "pressure")
  private Float pressure;

  public CC2650PressureSample() {
    // default
  }

  public CC2650PressureSample(String timestamp, String bdAddress) {
    this.timestamp = timestamp;
    this.bdAddress = bdAddress;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return this.lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getBdAddress() {
    return this.bdAddress;
  }

  public void setBdAddress(String bdAddress) {
    this.bdAddress = bdAddress;
  }

  public Float getTemperature() {
    return this.temperature;
  }

  public void setTemperature(Float temperature) {
    this.temperature = temperature;
  }

  public Float getPressure() {
    return this.pressure;
  }

  public void setPressure(Float pressure) {
    this.pressure = pressure;
  }

  @Override
  public String toString() {
    return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
        + "\",\"device\":\"" + this.bdAddress + "\",\"temperature\":{\"value\":" + this.temperature + ",\"unit\":\"" + UNIT_DEGREES_CELSIUS + "\"}"
        + ",\"pressure\":{\"value\":" + this.pressure + ",\"unit\":\"" + UNIT_HECTOPASCAL + "\"}}";
  }
}
