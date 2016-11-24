package de.fhg.fit.biomos.sensorplatform.sample;


import javax.persistence.*;
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
    @Column(name = "timestamp")
    private String timestamp;
    @Column(name = "device")
    private String bdAddress;
    @Column(name= "oxygenPercent")
    private Float oxygenPercent;
    @Column(name= "oxygenppO2")
    private Float oxygenppO2;


    public LuminoxOxygenSample() {
        // default
    }

    public LuminoxOxygenSample(String timestamp, String bdAddress) {
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
}
