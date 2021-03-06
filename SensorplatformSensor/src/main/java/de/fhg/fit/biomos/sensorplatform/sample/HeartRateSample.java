package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for heart rate samples.
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "HeartRateSample")
public class HeartRateSample implements Serializable {

  private static final long serialVersionUID = 850688777942461042L;

  private static final String BPM = "bpm";
  private static final String JOULE = "J";
  private static final String BPMMS = "bpm/ms";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "transmitted")
  private Boolean transmitted;
  @Column(name = "timestamp")
  private String timestamp;
  @Column(name = "device")
  private String bdAddress;
  @Column(name = "heartrate")
  private Integer heartRate;
  @Column(name = "energyexpended")
  private Integer energyExpended;
  @Column(name = "rrintervals")
  private String rrIntervals = "[]";

  public HeartRateSample() {
  }

  public HeartRateSample(String timeStamp, String bdAddress, boolean transmitted) {
    this.timestamp = timeStamp;
    this.bdAddress = bdAddress;
    this.transmitted = transmitted;
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

  public Boolean isTransmitted() {
    return this.transmitted;
  }

  public void setTransmitted(boolean transmitted) {
    this.transmitted = transmitted;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getBDaddress() {
    return this.bdAddress;
  }

  public void setBDaddress(String bdAddress) {
    this.bdAddress = bdAddress;
  }

  public Integer getHeartRate() {
    return this.heartRate;
  }

  public void setHeartRate(int heartRate) {
    this.heartRate = heartRate;
  }

  public Integer getEnergyExpended() {
    return this.energyExpended;
  }

  public void setEnergyExpended(int energyExpended) {
    this.energyExpended = energyExpended;
  }

  public String getRRintervals() {
    return this.rrIntervals;
  }

  public void setRRintervals(String rrIntervals) {
    this.rrIntervals = rrIntervals;
  }

  public void setRRintervals(List<Float> rrIntervals) {
    this.rrIntervals = rrIntervals.toString();
  }

  @Override
  public String toString() {
    return "{\"id\":" + this.id + ",\"transmitted\":" + this.transmitted + ",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
        + "\",\"timestamp\":\"" + this.timestamp + "\",\"device\":\"" + this.bdAddress + "\",\"value\":" + "{\"heartrate\":{\"value\":" + this.heartRate
        + ",\"unit\":\"" + BPM + "\"},\"energyexpended\":{\"value\":" + this.energyExpended + ",\"unit\":\"" + JOULE + "\"}" + ",\"rrintervals\":{\"value\":"
        + this.rrIntervals.toString() + ",\"unit\":\"" + BPMMS + "\"}}}";
  }

}
