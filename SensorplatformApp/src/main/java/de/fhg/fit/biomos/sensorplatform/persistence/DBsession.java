package de.fhg.fit.biomos.sensorplatform.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sample.CC2650AmbientlightSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650HumiditySample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650MovementSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650PressureSample;
import de.fhg.fit.biomos.sensorplatform.sample.CC2650TemperatureSample;
import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;
import de.fhg.fit.biomos.sensorplatform.sample.PulseOximeterSample;

/**
 *
 * @author Daniel Pyka
 *
 */
public class DBsession {

  private static final Logger LOG = LoggerFactory.getLogger(DBsession.class);

  private Session session;

  public DBsession(Session session) {
    this.session = session;
    this.session.beginTransaction();
  }

  public void commit() {
    Transaction transaction = this.session.getTransaction();
    if (transaction.getStatus() == TransactionStatus.ACTIVE) {
      transaction.commit();
    }
    this.session.beginTransaction();
  }

  public void rollback() {
    Transaction transaction = this.session.getTransaction();
    if (transaction.getStatus() == TransactionStatus.ACTIVE) {
      transaction.rollback();
    }
  }

  public void close() {
    this.session.close();
    this.session = null;
  }

  public void saveHeartRateSample(HeartRateSample hrs) {
    this.session.save(hrs);
  }

  public void savePulseOximeterSample(PulseOximeterSample hrs) {
    this.session.save(hrs);
  }

  public void saveCC2650AmbientlightSample(CC2650AmbientlightSample sample) {
    this.session.save(sample);
  }

  public void saveCC2650HumiditySample(CC2650HumiditySample sample) {
    this.session.save(sample);
  }

  public void saveCC2650MovementSample(CC2650MovementSample sample) {
    this.session.save(sample);
  }

  public void saveCC2650PressureAample(CC2650PressureSample sample) {
    this.session.save(sample);
  }

  public void saveCC2650TemperatureSample(CC2650TemperatureSample sample) {
    this.session.save(sample);
  }

  @SuppressWarnings("unchecked")
  public List<HeartRateSample> getHeartRateSamples() {
    LOG.info("get all heart rate samples");
    return this.session.createQuery("FROM HeartRateSample").getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<HeartRateSample> getNotTransmittedHeartRateSamples() {
    LOG.info("get all not transmitted heart rate samples");
    return this.session.createQuery("FROM HeartRateSample WHERE transmitted=false").getResultList();
  }

  @SuppressWarnings("deprecation")
  public int getNumberOfHeartRateSamples() {
    LOG.info("get total number of heart rate samples");
    return ((Long) this.session.createQuery("SELECT count(*) FROM HeartRateSample").uniqueResult()).intValue();
  }

  @SuppressWarnings("deprecation")
  public int getNumberOfNotTransmittedHeartRateSamples() {
    LOG.info("get number of not transmitted heart rate samples");
    return ((Long) this.session.createQuery("SELECT count(*) FROM HeartRateSample WHERE transmitted=false").uniqueResult()).intValue();
  }

  public int deleteAllHeartRateSamples() {
    LOG.info("delete all heart rate samples");
    return this.session.createQuery("DELETE * FROM HeartRateSample").executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PulseOximeterSample> getPulseOximeterSamples() {
    LOG.info("get all pulse oximeter samples");
    return this.session.createQuery("FROM PulseOximeterSample").getResultList();
  }

  public int deleteAllPulseOximeterSamples() {
    LOG.info("delete all pulse oximeter samples");
    return this.session.createQuery("DELETE * FROM PulseOximeterSample").executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<CC2650TemperatureSample> getCC2650TemperatureSamples() {
    LOG.info("get all CC2650 temperature samples");
    return this.session.createQuery("FROM CC2650TemperatureSample").getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<CC2650HumiditySample> getCC2650HumiditySamples() {
    LOG.info("get all CC2650 humdity samples");
    return this.session.createQuery("FROM CC2650HumiditySample").getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<CC2650PressureSample> getCC2650PressureSamples() {
    LOG.info("get all CC2650 pressure samples");
    return this.session.createQuery("FROM CC2650PressureSample").getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<CC2650AmbientlightSample> getCC2650AmbientlightSamples() {
    LOG.info("get all CC2650 ambientlight samples");
    return this.session.createQuery("FROM CC2650AmbientlightSample").getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<CC2650MovementSample> getCC2650MovementSamples() {
    LOG.info("get all CC2650 movement samples");
    return this.session.createQuery("FROM CC2650MovementSample").getResultList();
  }
}
