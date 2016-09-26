package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for temperature samples.
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "CC2650TemperatureSample")
public class CC2650TemperatureSample implements Serializable {

  private static final long serialVersionUID = -6784399975370306176L;

  private static final String UNIT_DEGREES_CELSIUS = "°C";
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
  @Column(name = "objecttemperature")
  private Float objectTemperature;
  @Column(name = "dietemperature")
  private Float dieTemperature;

  public CC2650TemperatureSample() {
    // default
  }

  public CC2650TemperatureSample(String timestamp, String bdAddress) {
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

  public Float getObjectTemperature() {
    return this.objectTemperature;
  }

  public void setObjectTemperature(Float objectTemperature) {
    this.objectTemperature = objectTemperature;
  }

  public Float getDieTemperature() {
    return this.dieTemperature;
  }

  public void setDieTemperature(Float dieTemperature) {
    this.dieTemperature = dieTemperature;
  }

  @Override
  public String toString() {
    return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
        + "\",\"device\":\"" + this.bdAddress + "\",\"objectTemperature\":{\"value\":" + this.objectTemperature + ",\"unit\":\"" + UNIT_DEGREES_CELSIUS + "\"}"
        + ",\"dieTemperature\":{\"value\":" + this.dieTemperature + ",\"unit\":\"" + UNIT_DEGREES_CELSIUS + "\"}}";
  }

}
