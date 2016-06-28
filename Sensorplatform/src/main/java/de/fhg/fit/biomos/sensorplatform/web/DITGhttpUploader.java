package de.fhg.fit.biomos.sensorplatform.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

/**
 *
 * @author Daniel Pyka
 *
 */
public class DITGhttpUploader implements HttpUploader {

  private static String url = "";

  public DITGhttpUploader() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void login() {
    try {
      HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
      httpsURLConnection.setDoInput(true);
      httpsURLConnection.setDoOutput(true);
      httpsURLConnection.setRequestMethod("POST");
      httpsURLConnection.setReadTimeout(0);
      httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
      OutputStream os = httpsURLConnection.getOutputStream();

      JSONObject content = new JSONObject();
      content.put("username", "SYML-TST-003-XXX");
      content.put("password", "a-secret");
      os.write(content.toString().getBytes());

    } catch (IOException e) {

    }
  }

  @Override
  public void send() {
    // TODO Auto-generated method stub

  }

}
