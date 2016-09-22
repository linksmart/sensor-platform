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
import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;

/**
 * REST service for retrieving CC2650 samples. Called from the frontend.
 *
 * @author Daniel Pyka
 *
 */
@Path("/cc2650")
public class CC2650Service {
  private static final Logger LOG = LoggerFactory.getLogger(CC2650Service.class);

  private final DBcontroller db;

  @Inject
  public CC2650Service(DBcontroller db) {
    this.db = db;
  }

  @Path("/temperature")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTemperatureSamples() {
    LOG.info("/cc2650/temperature called");
    DBsession s = this.db.getSession();
    List<CC2650TemperatureSample> sampleList = s.getCC2650TemperatureSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(sampleList.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad temperature sample format", e);
      return Response.serverError().build();
    }
  }

  @Path("/humidity")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getHumditySamples() {
    LOG.info("/cc2650/humidity called");
    DBsession s = this.db.getSession();
    List<CC2650HumiditySample> sampleList = s.getCC2650HumiditySamples();
    s.close();
    try {
      JSONArray response = new JSONArray(sampleList.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad humidity sample format", e);
      return Response.serverError().build();
    }
  }

  @Path("/pressure")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPressureSamples() {
    LOG.info("/cc2650/pressure called");
    DBsession s = this.db.getSession();
    List<CC2650PressureSample> sampleList = s.getCC2650PressureSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(sampleList.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad pressure sample format", e);
      return Response.serverError().build();
    }
  }

  @Path("/ambientlight")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAmbientlightSamples() {
    LOG.info("/cc2650/ambientlight called");
    DBsession s = this.db.getSession();
    List<CC2650AmbientlightSample> sampleList = s.getCC2650AmbientlightSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(sampleList.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad ambientlight sample format", e);
      return Response.serverError().build();
    }
  }

  @Path("/movement")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMovementSamples() {
    LOG.info("/cc2650/movement called");
    DBsession s = this.db.getSession();
    List<CC2650MovementSample> sampleList = s.getCC2650MovementSamples();
    s.close();
    try {
      JSONArray response = new JSONArray(sampleList.toString());
      return Response.ok(response).build();
    } catch (JSONException e) {
      LOG.error("bad movement sample format", e);
      return Response.serverError().build();
    }
  }

}
