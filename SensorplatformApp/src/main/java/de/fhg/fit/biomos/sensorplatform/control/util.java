package de.fhg.fit.biomos.sensorplatform.control;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

import java.io.IOException;
import java.util.Properties;


/**
 * Created by garagon on 20.02.2017.
 */
public class util {

    public static void main(String[] args) throws IOException {


        //ProcessBuilder tavo=new ProcessBuilder("ifdown","wlan0","&&","ifup","wlan0");
       // ProcessBuilder tavo=new ProcessBuilder("wpa_passphrase", "SSID", "password");
        Properties properties=new Properties();
        properties.put("version", "0.1-SNAPSHOT");
        properties.put("target.name", "SPF1");
        properties.put("webapp.port", "8080");
        properties.put("keystore.filename", "keystore.jks");
        properties.put("keystore.password", "123456");
        properties.put("webapp.username", "sensorplatform");
        properties.put("webapp.password", "123456");
        properties.put("http.useragent.boardname", "Junit test");
        properties.put("webinterface.name", "WebHrs");
        properties.put("webinterface.data.url", "http://webhrs.ddnss.de:12345/hrs/upload");
        properties.put("webinterface.timestamp.format", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println("Regreso: "+properties.getProperty("target.platform")+"ghshsg"+properties.getProperty("target.name"));
       /* try {
            Process pr=tavo.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }


}
