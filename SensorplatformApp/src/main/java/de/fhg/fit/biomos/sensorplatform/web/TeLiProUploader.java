package de.fhg.fit.biomos.sensorplatform.web;

import java.io.IOException;

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
 * Communication class for the TeLiPro webinterface. It basically provides a REST interface for logging in, which returns a cookie for authorisation and another
 * REST interface for sending samples.
 *
 * @author Daniel Pyka
 *
 */
public class TeLiProUploader implements Uploader {

  private static final Logger LOG = LoggerFactory.getLogger(TeLiProUploader.class);

  private final DateTimeFormatter dtf;

  private final String webinterfaceName;

  private final String userName;
  private final String password;
  private final String userAgent;

  private final String loginAddress;
  private final String dataAddress;

  private String authorizationToken = "";

  private final CloseableHttpClient httpclient;

  @Inject
  public TeLiProUploader(@Named("webinterface.name") String webinterfaceName, @Named("webinterface.username") String userName,
      @Named("webinterface.password") String password, @Named("http.useragent.boardname") String userAgent,
      @Named("webinterface.timestamp.format") String timestampFormat, @Named("webinterface.login.url") String loginAddress,
      @Named("webinterface.data.url") String dataAddress) {
    this.webinterfaceName = webinterfaceName;
    this.userName = userName;
    this.password = password;
    this.userAgent = userAgent + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");

    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);

    this.loginAddress = loginAddress;
    this.dataAddress = dataAddress;

    this.httpclient = HttpClients.createDefault();
  }

  @Override
  public String getWebinterfaceName() {
    return this.webinterfaceName;
  }

  /**
   * Only for testing! Download samples needs authorisation, too. Got some troubles using a REST-client. Therefor this piece of code is added by hand.
   *
   * @param webinterfaceDataDownloadURL
   *          the GET url with parameters what to download
   */
  @Deprecated
  public void downloadData(String webinterfaceDataDownloadURL) {
    HttpGet get = new HttpGet(webinterfaceDataDownloadURL);
    get.setHeader("Authorization", this.authorizationToken);

    try {
      CloseableHttpResponse response = this.httpclient.execute(get);
      System.out.println(EntityUtils.toString(response.getEntity()));
      response.close();
      LOG.info("download data successful");
    } catch (IOException e) {
      LOG.error("failed to download samples", e);
    }
  }

  @Override
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
        LOG.info("login successful");
      } else {
        LOG.error("login failed, error code: {}", statusCode);
        response.close();
      }
    } catch (IOException e) {
      LOG.error("login failed", e);
    }
  }

  @Override
  public int sendHeartRateSample(HeartRateSample hrs) throws IOException {
    HttpPost post = new HttpPost(this.dataAddress);
    post.setHeader("User-Agent", this.userAgent);
    post.setHeader("Authorization", this.authorizationToken);

    JSONObject content = createPOSTcontent(hrs.getBDaddress(), hrs.getHeartRate());
    StringEntity requestEntity = new StringEntity(content.toString(), ContentType.create("application/json", "UTF-8"));
    post.setEntity(requestEntity);

    CloseableHttpResponse response = this.httpclient.execute(post);
    int statusCode = response.getStatusLine().getStatusCode();
    response.close();
    return statusCode;
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
