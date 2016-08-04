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

@Path("/startup")
public class StartupService {

  private static final Logger LOG = LoggerFactory.getLogger(StartupService.class);

  private final Controller controller;

  @Inject
  public StartupService(Controller controller) {
    this.controller = controller;
  }

  @Path("/fromwebapp")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response startupFromWebApp(JSONObject request) {
    LOG.info("/startup/fromwebapp called");
    try {
      int uptime = request.getInt("uptime");
      // jetty uses only codehous json library
      org.json.JSONArray requestConverted = new org.json.JSONArray(request.getJSONArray("configuration").toString());
      this.controller.startupFromWebConfiguration(uptime * 1000, requestConverted, true);
      return Response.ok().build();
    } catch (JSONException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  @Path("/frombuild")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response startupFromProjectBuild() {
    LOG.info("/startup/frombuild called");

    try {
      this.controller.startupFromProjectBuildConfiguration(true);
      return Response.ok().build();
    } catch (RuntimeException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

}
