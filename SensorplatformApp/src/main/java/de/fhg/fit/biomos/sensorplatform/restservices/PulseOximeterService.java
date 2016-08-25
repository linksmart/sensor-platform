package de.fhg.fit.biomos.sensorplatform.restservices;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

/**
 *
 * @author Daniel Pyka
 *
 */
@Path("/pos")
public class PulseOximeterService {
  private static final Logger LOG = LoggerFactory.getLogger(PulseOximeterService.class);

  private final DBcontroller db;

  @Inject
  public PulseOximeterService(DBcontroller db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPos() {
    LOG.info("/pos called");
    DBsession s = this.db.getSession();
    List<PulseOximeterSample> poss = s.getPulseOximeterSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(poss.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad pulse oximeter samples format", e);
      return Response.serverError().build();
    }
  }

  @Path("/delete")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteAllPos() {
    LOG.info("/pos/delete called");
    DBsession s = this.db.getSession();
    int result = s.deleteAllPulseOximeterSamples();
    s.commit();
    s.close();
    JSONObject response = new JSONObject();
    try {
      response.put("result", result);
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad response JSONobject", e);
      return Response.serverError().build();
    }
  }
}
