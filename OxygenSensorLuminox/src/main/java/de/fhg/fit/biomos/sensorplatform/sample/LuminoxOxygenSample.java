package de.fhg.fit.biomos.sensorplatform.sample;


import eu.linksmart.services.payloads.SenML.Event;
import eu.linksmart.services.utils.serialization.DefaultSerializer;
import eu.linksmart.services.utils.serialization.Serializer;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * Entity class for oxygen, temperature and pressure samples
 *
 *
 * Created by garagon on 22.11.2016.
 */


@Entity
@Table(name = "LuminoxOxygenSensor")
public class LuminoxOxygenSample {
    private static final long serialVersionUID = 6429269084094852998L;

    private static final String UNIT_PERCENT_O2 = "%O2";
    private static final String UNIT_PERCENT_ppO2 = "ppO2";

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
    @Column(name= "oxygenPercent")
    private Float oxygenPercent;
    @Column(name= "oxygenppO2")
    private Float oxygenppO2;

    private String idExt;


    public LuminoxOxygenSample() {
        // default
    }

    public LuminoxOxygenSample(Long timeStamp, String bdAddress, boolean transmitted) {
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

    public Float getOxygenConcentrationPercent(){ return this.oxygenPercent; }

    public Float setOxygenConcentrationPercent(Float oxygen) {return this.oxygenPercent = oxygen;}

    public Float getOxygenConcentrationppO2(){ return this.oxygenppO2; }

    public Float setOxygenConcentrationppO2(Float oxygen) {return this.oxygenppO2 = oxygen;}





    @Override
    public String toString() {
        return "{\"id\":" + this.id + ",\"timestamp\":\"" + this.timestamp + "\",\"firstname\":\"" + this.firstname + "\",\"lastname\":\"" + this.lastname
                + "\",\"device\":\"" + this.bdAddress + "\",\"oxygen concentration\":{\"value\":" + this.oxygenPercent + ",\"unit\":\"" + UNIT_PERCENT_O2 + "\"}}"
                + "\",\"oxygen concentration\":{\"value\":" + this.oxygenppO2 + ",\"unit\":\"" + UNIT_PERCENT_ppO2 + "\"}}";
    }

    public String toStringLinkSmart() throws IOException {
        Serializer serializer = new DefaultSerializer();
        Event event=new Event();

        event.setBaseName("urn:dev:mac:"+this.bdAddress);
        event.setBt((long) 0);
        Event.Measurement measurement=new Event.Measurement();
        measurement.setN("/oxygen");
        measurement.setV(this.oxygenPercent);
        measurement.setU(UNIT_PERCENT_O2);
        measurement.setT((long)(this.timestamp/1000));
        event.setE(measurement);

        return serializer.toString(event);
       /* return "{\"" + "e"+"\":[{\"n\": \"oxygen\", \"v\": "+this.oxygenPercent+", \"u\": \"mBar\", \"t\": "+(long)(this.timestamp/1000)+"}],"+
                "\"bn\": \"urn:dev:mac:"+this.bdAddress+"/\"}";*/
    }
}
