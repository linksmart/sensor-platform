package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Daniel Pyka
 *
 */
@Entity
@Table(name = "PulseOximeterSample")
public class PulseOximeterSample implements Serializable {

  private static final long serialVersionUID = 4492772084482509666L;

  private static final String BPM = "bpm";
  private static final String PERCENT = "%";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "timestamp")
  private String timestamp;
  @Column(name = "device")
  private String bdAddress;
  @Column(name = "spo2")
  private Integer SpO2;
  @Column(name = "pulserate")
  private Integer PulseRate;

  public PulseOximeterSample() {
  }

  public PulseOximeterSample(String timestamp, String bdAddress) {
    this.timestamp = timestamp;
    this.bdAddress = bdAddress;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getSpO2() {
    return this.SpO2;
  }

  public void setSpO2(Integer spO2) {
    this.SpO2 = spO2;
  }

  public Integer getPulseRate() {
    return this.PulseRate;
  }

  public void setPulseRate(Integer pulseRate) {
    this.PulseRate = pulseRate;
  }

}
