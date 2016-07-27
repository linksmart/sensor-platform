package de.fhg.fit.biomos.sensorplatform.restservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

@Path("/info")
public class InfoService {

  // private static final Logger LOG = LoggerFactory.getLogger(InfoService.class);

  private final Controller controller;

  @Inject
  public InfoService(Controller controller) {
    this.controller = controller;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response info() {
    // LOG.info("/info called");

    JSONObject response = new JSONObject();
    try {
      response.put("recording", this.controller.isRecording());
    } catch (JSONException e) {
      e.printStackTrace();
      Response.serverError().build();
    }
    return Response.ok(response).build();
  }
}
