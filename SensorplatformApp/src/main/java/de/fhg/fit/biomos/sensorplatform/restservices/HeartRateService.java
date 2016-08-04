package de.fhg.fit.biomos.sensorplatform.restservices;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.persistence.DBsession;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

@Path("/samples")
public class HeartRateService {

  private static final Logger LOG = LoggerFactory.getLogger(HeartRateService.class);

  private final DBcontroller db;

  @Inject
  public HeartRateService(DBcontroller db) {
    this.db = db;
  }

  @Path("/hrs")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStoredHeartRateSamples() {
    DBsession s = this.db.getSession();
    List<HeartRateSample> hrss = s.getHeartRateSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(hrss.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad heartratesamples format", e);
      return Response.serverError().build();
    }
  }

}
