package de.fhg.fit.biomos.sensorplatform.restservices;

import java.util.List;

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

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.util.DetectedDevice;

/**
 * REST service for sending commands to the Sensorplatform Controller. Used by the frontend. Commands supported: start, stop, scan
 *
 * @author Daniel Pyka
 *
 */
@Path("/controller")
public class ControllerService {

  private static final Logger LOG = LoggerFactory.getLogger(ControllerService.class);

  private final Controller controller;

  @Inject
  public ControllerService(Controller controller) {
    this.controller = controller;
  }

  @Path("/start")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response start(JSONObject request) {
    LOG.info("/controller/start called");
    try {
      int uptime = request.getInt("uptime");
      String firstname = request.getString("firstname");
      String lastname = request.getString("lastname");
      // jetty uses only codehous json library
      // the rest of the application uses org.json library for cleaner code
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configuration").toString());
      String result = this.controller.startRecordingPeriod(uptime * 1000, firstname, lastname, requestConverted, true);
      JSONObject response = new JSONObject();
      response.put("result", result);
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad json from sensorplatform webinterface", e.getMessage());
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
