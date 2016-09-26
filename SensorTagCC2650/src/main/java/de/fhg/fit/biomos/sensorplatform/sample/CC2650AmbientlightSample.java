package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for ambient light samples.
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "CC2650AmbientlightSample")
public class CC2650AmbientlightSample implements Serializable {

  private static final long serialVersionUID = -786168161136914634L;

  private static final String UNIT_LUX = "lx";

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
  @Column(name = "ambientlight")
  private Float ambientlight;

  public CC2650AmbientlightSample() {
    // default
  }

  public CC2650AmbientlightSample(String timestamp, String bdAddress) {
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

  public Float getAmbientlight() {
    return this.ambientlight;
  }

  public void setAmbientlight(float ambientlight) {
    this.ambientlight = ambientlight;
  }

  @Override
  public String toString() {
    return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
        + "\",\"device\":\"" + this.bdAddress + "\",\"ambientlight\":{\"value\":" + this.ambientlight + ",\"unit\":\"" + UNIT_LUX + "\"}}";
  }

}
