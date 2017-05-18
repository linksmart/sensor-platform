package de.fhg.fit.biomos.sensorplatform.tools;

import eu.linksmart.services.payloads.SenML.Event;
import eu.linksmart.services.utils.serialization.DefaultSerializer;
import eu.linksmart.services.utils.serialization.Serializer;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by garagon on 18.05.2017.
 */
public class MQTTImpl {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTImpl.class);

    public MQTTImpl() {
    }

    public void MqttConnect(String broker, String uuid){
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient client = new MqttClient(broker, uuid, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            LOG.info("Connecting to broker: ", broker);
            client.connect(connOpts);
            LOG.info("Connected to broker");

        }catch (MqttException e){
            LOG.error("scan failed", e.getCause());

        }

    }

    public int MqttDisconnect(){
        return 0;
    }

    public void SPFMqttPublish(MqttClient client, String topic, Event event){


        Serializer serializer=new DefaultSerializer();

        try {
            client.publish(topic, serializer.serialize(event), 0, false);
            LOG.info("Message published");
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void SPFMqttSubscribe(MqttClient client, String topic, String data){

        Serializer serializer=new DefaultSerializer();


            try {
                client.subscribe(topic);

            } catch (MqttException e) {
                e.printStackTrace();
            }

    }


}
