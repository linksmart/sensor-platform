package de.fhg.fit.biomos.sensorplatform.restservices;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.system.HardwarePlatform;
import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

/**
 * REST service for sending commands to the Sensorplatform Controller. Also provides general information about the system to the frontend. Commands supported:
 * info, start, stop, scan.
 *
 * @author Daniel Pyka
 *
 */
@Path("/controller")
public class ControllerService {

  private static final Logger LOG = LoggerFactory.getLogger(ControllerService.class);

  private final Controller controller;
  private final HardwarePlatform hwPlatform;
  private final Uploader uploader;

  private final String sensorplatform;

  @Inject
  public ControllerService(Controller controller, HardwarePlatform hwPlatform, @Nullable Uploader uploader,
                           @Named("http.useragent.boardname") String userAgent) {
    this.controller = controller;
    this.hwPlatform = hwPlatform;
    this.sensorplatform = userAgent + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
    this.uploader = uploader;
  }

  @Path("/info")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response info() {
    JSONObject response = new JSONObject();
    try {
      response.put("recording", this.controller.isRecording());
      response.put("mobileinternet", this.hwPlatform.isConnectedToMobileInternet());
      response.put("overallrssi", this.hwPlatform.getOverallRSSIfromMobileInternet());
      response.put("rscp", this.hwPlatform.getSignalQualityBean().getRSCP());
      response.put("ecio", this.hwPlatform.getSignalQualityBean().getECIO());
      response.put("rssi", this.hwPlatform.getSignalQualityBean().getRSSI());
      if (this.uploader != null) {
        response.put("uploader", this.uploader.getWebinterfaceName());
      } else {
        response.put("uploader", "none");
      }
      response.put("sensorplatform", this.sensorplatform);
    } catch (JSONException e) {
      LOG.error("bad json response", e);
      Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @Path("/start")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response start(JSONObject request) {
    LOG.info("Stop all threads and processes");
    this.controller.interruptController();
    while(this.controller.isRecording()==true);
    LOG.info("/controller/start called");
    try {
      long uptime = request.getLong("uptime");
      String firstname = request.getString("firstname");
      String lastname = request.getString("lastname");
      // jetty uses only codehous json library
      // the rest of the application uses org.json library for cleaner code
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configuration").toString());
      //System.out.println("Test 1 : "+requestConverted);
      long uptimeMilli=uptime*1000;
      String result = this.controller.startRecordingPeriod(uptimeMilli, firstname, lastname, requestConverted, true);
      JSONObject response = new JSONObject();
      response.put("result", result);
      return Response.ok(response).build();

    } catch (JSONException e) {
      LOG.error("bad json from sensorplatform webinterface", e.getMessage());
      return Response.serverError().build();
    }
  }


  @Path("/wlan")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)

  public Response wlan(JSONObject request) {
    LOG.info("/controller/wlan called");
    try {
      // jetty uses only codehous json library
      // the rest of the application uses org.json library for cleaner code
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configurationWlan").toString());
      //System.out.println("Test 1 : "+requestConverted);
      String result = this.controller.changeWlanParameters(requestConverted, true);

      JSONObject response = new JSONObject();
      response.put("result", result);

      return Response.ok(response).build();

    } catch (JSONException e) {
      LOG.error("bad json from sensorplatform webinterface wlan", e.getMessage());
      return Response.serverError().build();
    }

  }


  @Path("/dongle")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)

  public Response dongle(JSONObject request) {
    LOG.info("/controller/dongle called");
    try {

      // jetty uses only codehous json library
      // the rest of the application uses org.json library for cleaner code
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configurationDongle").toString());
      //System.out.println("Test 1 : "+requestConverted);
      String result = this.controller.changeDongleParameters(requestConverted, true);

      JSONObject response = new JSONObject();
      response.put("result", result);

      return Response.ok(response).build();

    } catch (JSONException e) {
      LOG.error("bad json from sensorplatform webinterface dongle", e.getMessage());
      return Response.serverError().build();
    }

  }

  @Path("/stop")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response stop() {
    LOG.info("/controller/stop called");
    this.controller.interruptController();
    return Response.ok().build();
  }

  @Path("/scan")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response scan(@QueryParam("duration") String scanDuration) {
    LOG.info("/controller/scan called");
    List<DetectedDevice> detectedDevices = this.controller.scan(Integer.parseInt(scanDuration));
    try {
      JSONArray response = new JSONArray(detectedDevices.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad jsonarray format", e);
    }
    return Response.serverError().build();

  }

}
