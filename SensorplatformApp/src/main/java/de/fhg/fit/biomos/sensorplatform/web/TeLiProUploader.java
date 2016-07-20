package de.fhg.fit.biomos.sensorplatform.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import javax.net.ssl.HttpsURLConnection;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 * Communication class for the DITG webinterface. It basically provides a REST interface for logging in, which returns a cookie for authorisation and another
 * REST interface for sending samples.
 *
 * @author Daniel Pyka
 *
 */
public class TeLiProUploader implements Uploader {

  private static final Logger LOG = LoggerFactory.getLogger(TeLiProUploader.class);

  private final DateTimeFormatter dtf;

  private final String userName;
  private final String password;
  private final String userAgent;

  private final String loginAddress;
  private final String dataAddress;
  private final String dataDownloadAddress;

  private String authorizationToken = "";

  private final Queue<HeartRateSample> queue = new LinkedList<HeartRateSample>();

  public TeLiProUploader(Properties properties) {
    this.dtf = DateTimeFormat.forPattern(properties.getProperty("telipro.webinterface.timestamp.format")).withZone(DateTimeZone.UTC);
    LOG.info("timestamp pattern: " + properties.getProperty("telipro.webinterface.timestamp.format"));

    this.userName = properties.getProperty("telipro.webinterface.username");
    LOG.info("username: " + this.userName);
    this.password = properties.getProperty("telipro.webinterface.password");
    LOG.info("password: " + this.password);
    this.userAgent = properties.getProperty("http.useragent.boardname") + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " "
        + System.getProperty("os.version");
    LOG.info("user agent: " + this.userAgent);

    this.loginAddress = properties.getProperty("telipro.webinterface.login.url");
    LOG.info("login address: " + this.loginAddress);
    this.dataAddress = properties.getProperty("telipro.webinterface.data.url");
    LOG.info("data address: " + this.dataAddress);
    this.dataDownloadAddress = properties.getProperty("telipro.webinterface.data.download.url");
    LOG.info("download address: " + this.dataDownloadAddress);
  }

  /**
   * Enrich the sample with some additional data which is required for the DITG webinterface.
   *
   * @param bdAddress
   *          Bluetooth device address of the sensor
   * @param heartRate
   *          e.g. 60
   * @return JSONObject the whole POST content for the https connection
   */
  private JSONObject createPOSTcontent(String bdAddress, int heartRate) {
    JSONObject o = new JSONObject();
    o.put("type", "sample");
    o.put("sampleType", "quantity");
    o.put("quantityType", "HeartRate");
    o.put("value", heartRate);
    o.put("unit", "bpm");
    String format = this.dtf.print(new DateTime());
    o.put("startDate", format);
    o.put("endDate", format);
    o.put("metadata", new JSONObject().put("device", bdAddress));
    return o;
  }

  /**
   * Create a new https connection as POST to the given url.
   *
   * @param url
   *          the webinterface to connect to
   * @return HttpsURLConnection object
   * @throws IOException
   */
  private HttpsURLConnection newhttpsPostRequest(String url) throws IOException {
    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsURLConnection.setDoInput(true);
    httpsURLConnection.setDoOutput(true);
    httpsURLConnection.setRequestMethod("POST");
    httpsURLConnection.setReadTimeout(0);
    httpsURLConnection.setRequestProperty("User-Agent", this.userAgent);
    httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
    return httpsURLConnection;
  }

  /**
   * Only for testing! Required for downloading samples.
   *
   * @param url
   *          the webinterface to connect to
   * @return HttpsURLConnection object
   * @throws IOException
   */
  private HttpsURLConnection newhttpsGetRequest(String url) throws IOException {
    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
    httpsURLConnection.setDoInput(true);
    httpsURLConnection.setDoOutput(false);
    httpsURLConnection.setRequestMethod("GET");
    httpsURLConnection.setReadTimeout(0);
    httpsURLConnection.setRequestProperty("User-Agent", this.userAgent);
    httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
    return httpsURLConnection;
  }

  /**
   * Only for testing! Download samples needs authorisation, too. Got some troubles using a REST-client. Therefor this piece of code is added by hand.
   */
  @Deprecated
  public void downloadData() {
    login();
    try {
      HttpsURLConnection httpsURLConnection = newhttpsGetRequest(this.dataDownloadAddress);
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
  public void addToQueue(HeartRateSample hrs) {
    this.queue.add(hrs);
  }

  // TODO stop when queue is empty!!
  @Override
  public void run() {
    login();
    while (!Thread.currentThread().isInterrupted()) {
      HeartRateSample hrs = this.queue.poll();
      if (hrs != null) {
        sendData(hrs);
      } else {
        try {
          Thread.sleep(200);
          LOG.info("sleep occ");
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          LOG.info("upload thread interrupt received from SensorWrapper");
        }
      }
    }
    LOG.info("upload thread finished");
  }

  private boolean login() {
    try {
      HttpsURLConnection httpsURLConnection = newhttpsPostRequest(this.loginAddress);

      OutputStream os = httpsURLConnection.getOutputStream();
      JSONObject content = new JSONObject();
      content.put("username", this.userName);
      content.put("password", this.password);
      os.write(content.toString().getBytes());
      os.close();

      if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        this.authorizationToken = httpsURLConnection.getHeaderFields().get("Authorization").get(0);
        LOG.info("login successful - token acquired");
        // System.out.println(this.authorization); // extreme debugging
        httpsURLConnection.disconnect();
        return true;
      } else {
        LOG.error("login failed, error code: " + httpsURLConnection.getResponseCode());
        httpsURLConnection.disconnect();
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private void sendData(HeartRateSample hrs) {
    try {
      HttpsURLConnection httpsURLConnection = newhttpsPostRequest(this.dataAddress);

      httpsURLConnection.setRequestProperty("Authorization", this.authorizationToken);

      OutputStream os = httpsURLConnection.getOutputStream();
      os.write(createPOSTcontent(hrs.getBDaddress(), hrs.getHeartRate()).toString().getBytes());
      os.close();

      switch (httpsURLConnection.getResponseCode()) {
        case HttpURLConnection.HTTP_CREATED:
          LOG.info("sample transmission successful");
          break;
        case HttpURLConnection.HTTP_UNAUTHORIZED:
          LOG.error("transmission unauthorized - attempting to log in again");
          if (login()) {
            sendData(hrs);
          }
          break;
        default:
          LOG.error("transmission failed, error code: " + httpsURLConnection.getResponseCode());
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
