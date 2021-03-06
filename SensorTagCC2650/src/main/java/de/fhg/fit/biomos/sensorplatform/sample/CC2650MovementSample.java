package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class für movement samples.
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "CC2650MovementSample")
public class CC2650MovementSample implements Serializable {

  private static final long serialVersionUID = -584255602675210105L;

  private static final String UNIT_DEGREES_PER_SECOND = "deg/s";
  private static final String UNIT_GRAVITY = "G";
  private static final String UNIT_MICROTESLA = "uT";

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
  @Column(name = "rotationX")
  private Float rotationX;
  @Column(name = "rotationY")
  private Float rotationY;
  @Column(name = "rotationZ")
  private Float rotationZ;
  @Column(name = "accelerationX")
  private Float accelerationX;
  @Column(name = "accelerationY")
  private Float accelerationY;
  @Column(name = "accelerationZ")
  private Float accelerationZ;
  @Column(name = "magnetismX")
  private Float magnetismX;
  @Column(name = "magnetismY")
  private Float magnetismY;
  @Column(name = "magnetismZ")
  private Float magnetismZ;

  public CC2650MovementSample() {
    // default
  }

  public CC2650MovementSample(String timestamp, String bdAddress) {
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

  public Float getRotationX() {
    return this.rotationX;
  }

  public void setRotationX(Float rotationX) {
    this.rotationX = rotationX;
  }

  public Float getRotationY() {
    return this.rotationY;
  }

  public void setRotationY(Float rotationY) {
    this.rotationY = rotationY;
  }

  public Float getRotationZ() {
    return this.rotationZ;
  }

  public void setRotationZ(Float rotationZ) {
    this.rotationZ = rotationZ;
  }

  public Float getAccelerationX() {
    return this.accelerationX;
  }

  public void setAccelerationX(Float accelerationX) {
    this.accelerationX = accelerationX;
  }

  public Float getAccelerationY() {
    return this.accelerationY;
  }

  public void setAccelerationY(Float accelerationY) {
    this.accelerationY = accelerationY;
  }

  public Float getAccelerationZ() {
    return this.accelerationZ;
  }

  public void setAccelerationZ(Float accelerationZ) {
    this.accelerationZ = accelerationZ;
  }

  public Float getMagnetismX() {
    return this.magnetismX;
  }

  public void setMagnetismX(Float magnetismX) {
    this.magnetismX = magnetismX;
  }

  public Float getMagnetismY() {
    return this.magnetismY;
  }

  public void setMagnetismY(Float magnetismY) {
    this.magnetismY = magnetismY;
  }

  public Float getMagnetismZ() {
    return this.magnetismZ;
  }

  public void setMagnetismZ(Float magnetismZ) {
    this.magnetismZ = magnetismZ;
  }

  @Override
  public String toString() {
    return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
        + "\",\"device\":\"" + this.bdAddress + "\",\"value\":{\"rotation\":{\"value\":{\"x\":" + this.rotationX + ",\"y\":" + this.rotationY + ",\"z\":"
        + this.rotationZ + "},\"unit\":\"" + UNIT_DEGREES_PER_SECOND + "\"},\"acceleration\":{\"value\":{\"x\":" + this.accelerationX + ",\"y\":"
        + this.accelerationY + ",\"z\":" + this.accelerationZ + "},\"unit\":\"" + UNIT_GRAVITY + "\"},\"magnetism\":{\"value\":{\"x\":" + this.magnetismX
        + ",\"y\":" + this.magnetismY + ",\"z\":" + this.magnetismZ + "},\"unit\":\"" + UNIT_MICROTESLA + "\"}}}";
  }

}
