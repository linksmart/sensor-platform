package de.fhg.fit.biomos.sensorplatform.util;

import eu.linksmart.services.payloads.SenML.Event;
import eu.linksmart.services.utils.serialization.DefaultSerializer;
import eu.linksmart.services.utils.serialization.Serializer;

import java.io.IOException;

/**
 * Created by garagon on 17.05.2017.
 */
public class TestSenML {

    public String toStringLinkSmart(String bdAddress,float pressure) throws IOException {


        Serializer serializer = new DefaultSerializer();
        Event event=new Event();

        event.setBaseName("urn:dev:mac:"+bdAddress);
        long timestamp = System.currentTimeMillis();
        //.setBt((long)(timestamp/1000));
        event.setBt((long) 0);
        Event.Measurement measurement=new Event.Measurement();
        measurement.setN("airPressure");
        measurement.setV(pressure);
        measurement.setU("mBar");
        measurement.setT((long)(timestamp/1000));
        event.setE(measurement);

        return serializer.toString(event);

       /* return "{\"" + "e"+"\":[{\"n\": \"airPressure\", \"v\": "+this.pressure+", \"u\": \"mBar\", \"t\": "+(long)(this.timestamp/1000)+"}],"+
                "\"bn\": \"urn:dev:mac:"+this.bdAddress+"/\"}"; */

        //return "";

    }
}
