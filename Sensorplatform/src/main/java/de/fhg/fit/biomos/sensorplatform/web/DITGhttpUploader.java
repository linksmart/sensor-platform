package de.fhg.fit.biomos.sensorplatform.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Pyka
 *
 */
public class DITGhttpUploader implements HttpUploader {

  private static final Logger LOG = LoggerFactory.getLogger(DITGhttpUploader.class);

  private final SimpleDateFormat formatter;

  private final String userName;
  private final String password;

  private final String loginAddress;
  private final String dataAddress;
  private final String dataDownloadAddress;

  private String authorizationToken = "";

  public DITGhttpUploader(Properties properties) {
    this.formatter = new SimpleDateFormat(properties.getProperty("ditg.webinterface.timestamp.format"));
    LOG.info("time stamp pattern: " + this.formatter.toPattern());

    this.userName = properties.getProperty("ditg.webinterface.username");
    LOG.info("username: " + this.userName);
    this.password = properties.getProperty("ditg.webinterface.password");
    LOG.info("password: " + this.password);

    this.loginAddress = properties.getProperty("ditg.webinterface.login.url");
    LOG.info("login address: " + this.loginAddress);
    this.dataAddress = properties.getProperty("ditg.webinterface.data.url");
    LOG.info("data address: " + this.dataAddress);
    this.dataDownloadAddress = properties.getProperty("ditg.webinterface.data.download.url");
    LOG.info("download address: " + this.dataDownloadAddress);
  }

  private JSONObject makeJSON(String bdAddress, String quantityType, String value, String unit) {
    JSONObject o = new JSONObject();
    o.put("type", "sample");
    o.put("sampleType", "quantity");
    o.put("quantityType", quantityType);
    o.put("value", value);
    o.put("unit", unit);
    String format = this.formatter.format(Calendar.getInstance().getTime());
    o.put("startDate", format);
    o.put("endDate", format);
    o.put("metadata", new JSONObject().put("device", bdAddress));
    return o;
  }

  private HttpsURLConnection httpsPostRequest(String url) throws IOException {
    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsURLConnection.setDoInput(true);
    httpsURLConnection.setDoOutput(true);
    httpsURLConnection.setRequestMethod("POST");
    httpsURLConnection.setReadTimeout(0);
    httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
    return httpsURLConnection;
  }

  private HttpsURLConnection httpsGetRequest(String url) throws IOException {
    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsURLConnection.setDoInput(true);
    httpsURLConnection.setDoOutput(false);
    httpsURLConnection.setRequestMethod("GET");
    httpsURLConnection.setReadTimeout(0);
    httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
    return httpsURLConnection;
  }

  public void downloadData() {
    try {
      HttpsURLConnection httpsURLConnection = httpsGetRequest(this.dataDownloadAddress);
      httpsURLConnection.setRequestProperty("Authorization", this.authorizationToken);

      BufferedReader br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
      StringBuilder responseStrBuilder = new StringBuilder();
      String responseString;
      while ((responseString = br.readLine()) != null) {
        responseStrBuilder.append(responseString);
      }
      JSONArray content = new JSONArray(responseStrBuilder.toString());
      LOG.info("download data successful");
      System.out.println(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void login() {
    try {
      HttpsURLConnection httpsURLConnection = httpsPostRequest(this.loginAddress);

      OutputStream os = httpsURLConnection.getOutputStream();
      JSONObject content = new JSONObject();
      content.put("username", this.userName);
      content.put("password", this.password);
      os.write(content.toString().getBytes());
      os.close();

      if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        this.authorizationToken = httpsURLConnection.getHeaderFields().get("Authorization").get(0);
        LOG.info("login successful - token acquired");
        // System.out.println(this.authorization);
      } else {
        LOG.error("login failed, error code: " + httpsURLConnection.getResponseCode());
      }

      httpsURLConnection.disconnect();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendData(String bdAddress, String quantityType, String value, String unit) {
    try {
      HttpsURLConnection httpsURLConnection = httpsPostRequest(this.dataAddress);

      httpsURLConnection.setRequestProperty("Authorization", this.authorizationToken);

      OutputStream os = httpsURLConnection.getOutputStream();
      os.write(makeJSON(bdAddress, quantityType, value, unit).toString().getBytes());
      os.close();

      if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
        LOG.info("sample transmission successful");
      } else {
        LOG.error("transmission failed, error code: " + httpsURLConnection.getResponseCode());
        Map<String, List<String>> headerFields = httpsURLConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
          System.out.println(entry.getKey() + ":" + entry.getValue());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
