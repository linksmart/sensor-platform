package de.fhg.fit.biomos.sensorplatform.restservices;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;

@Path("/info")
public class InfoService {

  private final Controller controller;
  private final Uploader uploader;

  private final String sensorplatform;

  @Inject
  public InfoService(Controller controller, @Nullable Uploader uploader, @Named("http.useragent.boardname") String userAgent) {
    this.controller = controller;
    this.sensorplatform = userAgent + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
    this.uploader = uploader;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response info() {
    JSONObject response = new JSONObject();
    try {
      response.put("status", this.controller.isRecording());
      if (this.uploader != null) {
        response.put("uploader", this.uploader.getWebinterfaceName());
      } else {
        response.put("uploader", "none");
      }
      response.put("sensorplatform", this.sensorplatform);
    } catch (JSONException e) {
      e.printStackTrace();
      Response.serverError().build();
    }
    return Response.ok(response).build();
  }
}