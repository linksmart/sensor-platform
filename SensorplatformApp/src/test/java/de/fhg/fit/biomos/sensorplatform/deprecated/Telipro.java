package de.fhg.fit.biomos.sensorplatform.deprecated;

import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;

public class Telipro {

  public static void main(String[] args) {
    TeLiProUploader u = new TeLiProUploader("TeLiPro", "SYML-TST-003-XXX", "a-secret", "Sensorplatform", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'",
        "https://ditg.fit.fraunhofer.de/api/v1/deviceLogin", "https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples");

    u.login();

    u.downloadData("https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples/HeartRate/date/2016-07-27/1d.json");

  }

}
