package de.fhg.fit.biomos.sensorplatform.sample;

import javax.persistence.*;


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

    public String toStringLinkSmart() {
       // return "{\"id\":" + this.idExt+"_"+this.bdAddress + ",\"timestamp\":\"" + this.timestamp +",\"temperature\":"+this.temperature+",\"unit_temp\":\"" + UNIT_DEGREES_CELSIUS + "\"}";

      /*  return "{\"" + this.idExt+"\":[{\"n\": \"temperature\", \"v\": "+this.temperature+", \"u\": \"Cel\", \"t\": 0 },"+
                "\"bn\": \""+this.bdAddress+"\","+
                "\"bt\": \""+this.timestamp+"\","+
                "\"ver\": 1 }";

        return "{\"" + "e"+"\":[{\"n\": \"temperature\", \"v\": "+this.temperature+", \"u\": \"Cel\", \"t\": 0 , \"sv\":\"SPF2\"}],"+
                "\"bn\": \""+this.bdAddress+"/\","+
                "\"bt\": \""+this.timestamp+"\","+
                "\"ver\": 1 }";
*/
        return "{\"" + "e"+"\":[{\"n\": \"temperature\", \"v\": "+this.temperature+", \"u\": \"mBar\", \"t\": "+(long)(this.timestamp/1000)+"}],"+
                "\"bn\": \""+this.bdAddress+"/\"}";
    }

}
