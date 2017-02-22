package de.fhg.fit.biomos.sensorplatform.control;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

import java.io.IOException;


/**
 * Created by garagon on 20.02.2017.
 */
public class util {

    public static void main(String[] args) throws IOException {


        //ProcessBuilder tavo=new ProcessBuilder("ifdown","wlan0","&&","ifup","wlan0");
        ProcessBuilder tavo=new ProcessBuilder("wpa_passphrase", "SSID", "password");

        System.out.println("Regreso: "+tavo.toString());
       /* try {
            Process pr=tavo.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }


}
