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
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

/**
 *
 * @author Daniel Pyka
 *
 */
@Path("/hrs")
public class HeartRateService {

  private static final Logger LOG = LoggerFactory.getLogger(HeartRateService.class);

  private final DBcontroller db;

  @Inject
  public HeartRateService(DBcontroller db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getHRS() {
    LOG.info("/hrs called");
    DBsession s = this.db.getSession();
    List<HeartRateSample> hrss = s.getHeartRateSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(hrss.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad heart rate samples format", e);
      return Response.serverError().build();
    }
  }

  @Path("/delete")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteAllHRS() {
    LOG.info("/hrs/delete called");
    DBsession s = this.db.getSession();
    int result = s.deleteAllHeartRateSamples();
    LOG.info("result " + result);
    s.commit();
    s.close();
    return Response.ok().build();
  }

  @Path("/nottransmitted")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNotTrasmittedHRS() {
    LOG.info("/hrs/nottransmitted called");
    DBsession s = this.db.getSession();
    List<HeartRateSample> hrss = s.getNotTransmittedHeartRateSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(hrss.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad heart rate samples format", e);
      return Response.serverError().build();
    }
  }

  @Path("/numberofnottransmitted")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNumberOfNotTrasmittedHRS() {
    LOG.info("/hrs/nottransmitted called");
    DBsession s = this.db.getSession();
    int total = s.getNumberOfHeartRateSamples();
    int nottransmitted = s.getNumberOfNotTransmittedHeartRateSamples();
    s.close();
    JSONObject response = new JSONObject();
    try {
      response.put("total", total);
      response.put("nottransmitted", nottransmitted);
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad heart rate samples format", e);
      return Response.serverError().build();
    }
  }

}
