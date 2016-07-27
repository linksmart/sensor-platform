package de.fhg.fit.biomos.sensorplatform.web;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 * Communication class for the DITG webinterface. It basically provides a REST interface for logging in, which returns a cookie for authorisation and another
 * REST interface for sending samples.<br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author Daniel Pyka
 *
 */
public class TeLiProUploader implements Uploader {

  private static final Logger LOG = LoggerFactory.getLogger(TeLiProUploader.class);

  private static final int UPLOAD_THREAD_SLEEP_TIME_MS = 300;

  private final DateTimeFormatter dtf;

  private final String userName;
  private final String password;
  private final String userAgent;

  private final String loginAddress;
  private final String dataAddress;
  private final String dataDownloadAddress;

  private String authorizationToken = "";

  private boolean loggedIn = false;

  private final CloseableHttpClient httpclient;

  private final Queue<HeartRateSample> queue = new LinkedList<HeartRateSample>();
  // private final List<HeartRateSample> list = new ArrayList<HeartRateSample>();

  @Inject
  public TeLiProUploader(@Named("webinterface.username") String userName, @Named("webinterface.password") String password,
      @Named("http.useragent.boardname") String userAgent, @Named("webinterface.timestamp.format") String timestampFormat,
      @Named("webinterface.login.url") String loginAddress, @Named("webinterface.data.url") String dataAddress,
      @Named("webinterface.data.download.url") String dataDownloadAddress) {
    this.userName = userName;
    LOG.info("username: " + this.userName);
    this.password = password;
    LOG.info("password: " + this.password);
    this.userAgent = userAgent + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
    LOG.info("user agent: " + this.userAgent);

    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);
    LOG.info("timestamp pattern: " + timestampFormat);

    this.loginAddress = loginAddress;
    LOG.info("login address: " + this.loginAddress);
    this.dataAddress = dataAddress;
    LOG.info("data address: " + this.dataAddress);
    this.dataDownloadAddress = dataDownloadAddress;
    LOG.info("download address: " + this.dataDownloadAddress);

    this.httpclient = HttpClients.createDefault();
  }

  @Override
  public void addToQueue(HeartRateSample hrs) {
    this.queue.add(hrs);
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      if (!this.loggedIn) {
        login();
      }
      if (!this.queue.isEmpty()) {
        sendData(this.queue.peek());
      } else {
        try {
          Thread.sleep(UPLOAD_THREAD_SLEEP_TIME_MS);
        } catch (InterruptedException e) {
          LOG.info("interrupt received from Controller");
          while (!this.queue.isEmpty()) {
            LOG.info("sending remaining samples");
            sendData(this.queue.poll());
          }
          Thread.currentThread().interrupt();
        }
      }
    }
    LOG.info("upload thread finished");
  }

  /**
   * Only for testing! Download samples needs authorisation, too. Got some troubles using a REST-client. Therefor this piece of code is added by hand.
   */
  @Deprecated
  public void downloadData() {
    if (!this.loggedIn) {
      login();
    }
    HttpGet get = new HttpGet(this.dataDownloadAddress);
    get.setHeader("Authorization", this.authorizationToken);

    try {
      CloseableHttpResponse response = this.httpclient.execute(get);
      System.out.println(EntityUtils.toString(response.getEntity()));
      response.close();
      LOG.info("download data successful");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void login() {
    HttpPost post = new HttpPost(this.loginAddress);
    post.setHeader("User-Agent", this.userAgent);

    JSONObject content = new JSONObject();
    content.put("username", this.userName);
    content.put("password", this.password);

    StringEntity requestEntity = new StringEntity(content.toString(), ContentType.create("application/json", "UTF-8"));
    post.setEntity(requestEntity);

    try {
      CloseableHttpResponse response = this.httpclient.execute(post);

      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        this.authorizationToken = response.getFirstHeader("Authorization").getValue();
        response.close();
        this.loggedIn = true;
        LOG.info("login successful");
      } else {
        LOG.error("login failed, error code: " + statusCode);
        response.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendData(HeartRateSample hrs) {
    HttpPost post = new HttpPost(this.dataAddress);
    post.setHeader("User-Agent", this.userAgent);
    post.setHeader("Authorization", this.authorizationToken);

    JSONObject content = createPOSTcontent(hrs.getBDaddress(), hrs.getHeartRate());
    StringEntity requestEntity = new StringEntity(content.toString(), ContentType.create("application/json", "UTF-8"));
    post.setEntity(requestEntity);

    try {
      CloseableHttpResponse response = this.httpclient.execute(post);
      int statusCode = response.getStatusLine().getStatusCode();

      switch (statusCode) {
        case HttpStatus.SC_CREATED:
          LOG.info("sample transmission successful");
          this.queue.poll();
          break;
        case HttpStatus.SC_UNAUTHORIZED:
          LOG.error("transmission unauthorized - attempting to log in again");
          this.loggedIn = false;
          break;
        default:
          LOG.error("transmission failed, error code: " + statusCode);
          break;
      }
      response.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

}
