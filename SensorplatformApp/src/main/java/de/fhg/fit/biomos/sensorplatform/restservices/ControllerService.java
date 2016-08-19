package de.fhg.fit.biomos.sensorplatform.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

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
  @Produces(MediaType.TEXT_PLAIN)
  public Response start(JSONObject request) {
    LOG.info("/controller/start called");
    try {
      int uptime = request.getInt("uptime");
      // jetty uses only codehous json library
      // the rest of the application uses org.json library for cleaner code
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configuration").toString());
      this.controller.startup(uptime * 1000, requestConverted, true);
      return Response.ok().build();
    } catch (JSONException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  @Path("/stop")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response stop() {
    LOG.info("/controller/stop called");
    this.controller.interruptController();
    return Response.ok().build();
  }

}
