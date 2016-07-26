package de.fhg.fit.biomos.sensorplatform.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
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
  public Response startupFromWebApp(JSONArray request) {
    LOG.info("/startup/fromwebapp called");
    System.out.println(request);
    // it is not possible to use org.json.JSONArray as input parameter of this REST call
    // therefor we convert from one json library to the other
    org.json.JSONArray requestConverted = new org.json.JSONArray(request.toString());
    System.out.println(requestConverted);

    try {
      this.controller.startupFromWebConfiguration(10, 5, requestConverted);
      return Response.ok().build();
    } catch (RuntimeException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  @Path("/frombuild")
  @GET
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response startupFromProjectBuild() {
    LOG.info("/startup/frombuild called");

    try {
      this.controller.startupFromProjectBuildConfiguration();
      return Response.ok().build();
    } catch (RuntimeException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

}
