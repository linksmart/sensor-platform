package de.fhg.fit.biomos.sensorplatform.sample;

import eu.linksmart.services.payloads.SenML.Event;
import eu.linksmart.services.utils.serialization.DefaultSerializer;
import eu.linksmart.services.utils.serialization.Serializer;

import javax.persistence.*;
import java.io.IOException;


/**
 * Entity class for oxygen, temperature and pressure samples
 *
 *
 * Created by garagon on 22.11.2016.
 */
@Entity
@Table(name = "LuminoxTemperature")
public class LuminoxTemperatureSample {

    private static final long serialVersionUID = 6429269084094852998L;

    private static final String UNIT_DEGREES_CELSIUS = "°C";

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
    private Long timestamp;
    @Column(name = "device")
    private String bdAddress;
    @Column(name = "temperature")
    private Float temperature;

    private String idExt;


    public LuminoxTemperatureSample() {
        // default
    }

    public LuminoxTemperatureSample(Long timeStamp, String bdAddress, boolean transmitted) {
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

    public String getIdExt() {return this.idExt;}

    public void setIdExt(String idExt){this.idExt=idExt;}

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

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
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


    @Override


    public String toString() {

        return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
                + "\",\"device\":\"" + this.bdAddress + "\",\"temperature\":{\"value\":" + this.temperature + ",\"unit\":\"" + UNIT_DEGREES_CELSIUS + "\"}";
    }

    public String toStringLinkSmart() throws IOException {
        Serializer serializer = new DefaultSerializer();
        Event event=new Event();

        event.setBaseName("urn:dev:mac:"+this.bdAddress);
        event.setBt((long) 0);
        Event.Measurement measurement=new Event.Measurement();
        measurement.setN("temperature");
        measurement.setV(this.temperature);
        measurement.setU(UNIT_DEGREES_CELSIUS);
        measurement.setT((long)(this.timestamp/1000));
        event.setE(measurement);

        return serializer.toString(event);
      /*  return "{\"" + "e"+"\":[{\"n\": \"temperature\", \"v\": "+this.temperature+", \"u\": \"mBar\", \"t\": "+(long)(this.timestamp/1000)+"}],"+
                "\"bn\": \"urn:dev:mac:"+this.bdAddress+"/\"}";*/
    }

}
