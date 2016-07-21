package de.fhg.fit.biomos.sensorplatform.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  @Produces(MediaType.APPLICATION_JSON)
  public Response startupFromWebApp(JSONObject request) throws Exception {
    LOG.info("/startup/fromwebapp called");

    JSONObject response = new JSONObject();
    try {
      this.controller.startupFromWebConfiguration(10, 5, null);
      response.put("startup", "success");
    } catch (RuntimeException e) {
      response.put("startup", "error");
    }
    return Response.ok(response).build();
  }

  @Path("/frombuild")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response startupFromProjectBuild(JSONObject request) throws Exception {
    LOG.info("/startup/frombuild called");

    JSONObject response = new JSONObject();
    try {
      this.controller.startupFromProjectBuildConfiguration();
      response.put("startup", "success");
    } catch (RuntimeException e) {
      response.put("startup", "error");
    }

    return Response.ok(response).build();
  }

}
