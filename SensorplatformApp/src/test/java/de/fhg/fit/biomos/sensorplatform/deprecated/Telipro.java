package de.fhg.fit.biomos.sensorplatform.deprecated;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;

public class Telipro {

  public static void main(String[] args) {
    TeLiProUploader u = new TeLiProUploader("SYML-TST-003-XXX", "a-secret", "Sensorplatform", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'",
        "https://ditg.fit.fraunhofer.de/api/v1/deviceLogin", "https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples",
        "https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples/HeartRate/date/2016-07-26/1d.json");

    u.login();

    HeartRateSample hrs = new HeartRateSample("2016-07-26T:10:50:30:555Z", "11:22:33:44:55:66", false);
    hrs.setHeartRate(60);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);
    hrs.setHeartRate(hrs.getHeartRate() + 1);
    u.sendData(hrs);

  }

}
