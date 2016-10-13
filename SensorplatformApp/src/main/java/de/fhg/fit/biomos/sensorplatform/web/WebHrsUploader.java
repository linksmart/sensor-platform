package de.fhg.fit.biomos.sensorplatform.web;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
 * This implementation of Uploader is used for a private webserver as a backup solution and for testing.
 *
 * @author Daniel Pyka
 *
 */
public class WebHrsUploader implements Uploader {

  private static final Logger LOG = LoggerFactory.getLogger(WebHrsUploader.class);

  private final DateTimeFormatter dtf;

  private final String webinterfaceName;

  private final String userAgent;

  private final String dataAddress;

  private final CloseableHttpClient httpclient;

  @Inject
  public WebHrsUploader(@Named("webinterface.name") String webinterfaceName, @Named("webinterface.timestamp.format") String timestampFormat,
      @Named("http.useragent.boardname") String userAgent, @Named("webinterface.data.url") String dataAddress) {
    this.webinterfaceName = webinterfaceName;
    this.userAgent = userAgent + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
    this.dataAddress = dataAddress;

    this.dtf = DateTimeFormat.forPattern(timestampFormat).withZone(DateTimeZone.UTC);

    this.httpclient = HttpClients.createDefault();
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

  @Override
  public String getWebinterfaceName() {
    return this.webinterfaceName;
  }

  @Override
  @Deprecated
  public void login() {
  }

  @Override
  public int sendHeartRateSample(HeartRateSample hrs) throws IOException {
    long before = System.currentTimeMillis();
    HttpPost post = new HttpPost(this.dataAddress);
    post.setHeader("User-Agent", this.userAgent);

    JSONObject content = createPOSTcontent(hrs.getBDaddress(), hrs.getHeartRate());
    StringEntity requestEntity = new StringEntity(content.toString(), ContentType.create("application/json", "UTF-8"));
    post.setEntity(requestEntity);

    CloseableHttpResponse response = this.httpclient.execute(post);
    int statusCode = response.getStatusLine().getStatusCode();
    response.close();
    long after = System.currentTimeMillis();
    LOG.info("Http post request result {} in {} ms", statusCode, (after - before));
    return statusCode;
  }

}
