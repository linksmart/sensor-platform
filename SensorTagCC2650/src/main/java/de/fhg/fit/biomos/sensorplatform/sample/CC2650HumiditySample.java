package de.fhg.fit.biomos.sensorplatform.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CC2650HumiditySample")
public class CC2650HumiditySample implements Serializable {

  private static final long serialVersionUID = 6438103778130827022L;

  private static final String UNIT_DEGREES_CELSIUS = "°C";
  private static final String UNIT_RELATIVE_HUMIDITY = "%RH";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "timestamp")
  private String timestamp;
  @Column(name = "device")
  private String bdAddress;
  @Column(name = "temperature")
  private Float temperature;
  @Column(name = "humidity")
  private Float humidity;

  public CC2650HumiditySample() {
    // default
  }

  public CC2650HumiditySample(String timestamp, String bdAddress) {
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

  public Float getHumidity() {
    return this.humidity;
  }

  public void setHumidity(Float humidity) {
    this.humidity = humidity;
  }

  @Override
  public String toString() {
    return "{\"timestamp\":\" " + this.timestamp + "\",\"device\":\"" + this.bdAddress + ",\"temperature\":{\"value\":" + this.temperature + ",\"unit\":\""
        + UNIT_DEGREES_CELSIUS + "\"}" + ",\"humidity\":{\"value\":" + this.humidity + ",\"unit\":\"" + UNIT_RELATIVE_HUMIDITY + "\"}}";
  }

}
