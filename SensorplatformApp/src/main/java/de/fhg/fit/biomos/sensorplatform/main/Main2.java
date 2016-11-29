package de.fhg.fit.biomos.sensorplatform.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by garagon on 28.11.2016.
 */
public class Main2 {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String propertiesFileName = "SensorplatformApp.properties";

    private final Properties properties = new Properties();

    public static void main(String[] args) {
        Main sensorplatform = new Main();

    }

    private static void test() throws MalformedURLException {

    }
}
