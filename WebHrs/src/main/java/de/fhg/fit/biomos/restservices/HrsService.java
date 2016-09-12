package de.fhg.fit.biomos.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Pyka
 *
 */
@Path("/hrs")
public class HrsService {

  private static final Logger LOG = LoggerFactory.getLogger(HrsService.class);

  @Path("upload")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response hrs(JSONObject request) {
    LOG.info("/hrs/upload called");
    if (request.has("type") && request.has("sampleType") && request.has("quantityType") && request.has("value") && request.has("unit")
        && request.has("startDate") && request.has("endDate") && request.has("metadata")) {

    }
    System.out.println(request);
    return Response.ok().build();
  }
}
