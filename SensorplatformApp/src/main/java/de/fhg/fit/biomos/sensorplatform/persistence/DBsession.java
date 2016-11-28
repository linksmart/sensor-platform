package de.fhg.fit.biomos.sensorplatform.persistence;

import java.util.List;

import de.fhg.fit.biomos.sensorplatform.sample.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exposes basic functions for database interaction. It is instantiated by the DBcontroller (factory). Use one DBsession for one database interaction and throw
 * it away afterwards.
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

  /**
   * Commit the changes to the database.
   */
  public void commit() {
    Transaction transaction = this.session.getTransaction();
    if (transaction.getStatus() == TransactionStatus.ACTIVE) {
      transaction.commit();
    }
    this.session.beginTransaction();
  }

  /**
   * Shut down database gracefully. Hsqldb will merge log and script file.
   */
  @SuppressWarnings("deprecation")
  public void shutdown() {
    this.session.createSQLQuery("SHUTDOWN").executeUpdate();
  }

  /**
   * Close the current session.
   */
  public void close() {
    this.session.close();
    this.session = null;
  }

  /**
   * Save a HeartRateSample to the database.
   *
   * @param hrs
   *          HeartRateSample
   */
  public void saveHeartRateSample(HeartRateSample hrs) {
    this.session.saveOrUpdate(hrs);
  }

  /**
   * Save a PulseOximeterSample to the database.
   *
   * @param pos
   *          PulseOximeterSample
   */
  public void savePulseOximeterSample(PulseOximeterSample pos) {
    this.session.saveOrUpdate(pos);
  }

  /**
   * Save a CC2650AmbientlightSample to the database.
   *
   * @param sample
   *          CC2650AmbientlightSample
   */
  public void saveCC2650AmbientlightSample(CC2650AmbientlightSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650HumiditySample to the database.
   *
   * @param sample
   *          CC2650HumiditySample
   */
  public void saveCC2650HumiditySample(CC2650HumiditySample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650MovementSample to the database.
   *
   * @param sample
   *          CC2650MovementSample
   */
  public void saveCC2650MovementSample(CC2650MovementSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650PressureSample to the database.
   *
   * @param sample
   *          CC2650PressureSample
   */
  public void saveCC2650PressureSample(CC2650PressureSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650TemperatureSample to the database.
   *
   * @param sample
   *          CC2650TemperatureSample
   */
  public void saveCC2650TemperatureSample(CC2650TemperatureSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650TemperatureSample to the database.
   *
   * @param sample
   *          CC2650TemperatureSample
   */
  public void saveLuminoxTemperatureSample(LuminoxTemperatureSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650TemperatureSample to the database.
   *
   * @param sample
   *          CC2650TemperatureSample
   */
  public void saveLuminoxOxygenSample(LuminoxOxygenSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Save a CC2650TemperatureSample to the database.
   *
   * @param sample
   *          CC2650TemperatureSample
   */
  public void saveLuminoxAirPressureSample(LuminoxAirPressureSample sample) {
    this.session.saveOrUpdate(sample);
  }

  /**
   * Retrieve all HeartRateSamples stored in the database.
   *
   * @return List of HeartRateSamples
   */
  @SuppressWarnings("unchecked")
  public List<HeartRateSample> getHeartRateSamples() {
    LOG.info("get all heart rate samples");
    return this.session.createQuery("FROM HeartRateSample").getResultList();
  }

  /**
   * Retrieve all HeartRateSamples stored in the database, which are flagged transmitted=false.
   *
   * @return List of HeartRateSamples
   */
  @SuppressWarnings("unchecked")
  public List<HeartRateSample> getNotTransmittedHeartRateSamples() {
    LOG.info("get all not transmitted heart rate samples");
    return this.session.createQuery("FROM HeartRateSample WHERE transmitted=false").getResultList();
  }

  /**
   * Get the number of HeartRateSamples stored in the database.
   *
   * @return int number of heart rate samples
   */
  @SuppressWarnings("deprecation")
  public int getNumberOfHeartRateSamples() {
    LOG.info("get total number of heart rate samples");
    return ((Long) this.session.createQuery("SELECT count(*) FROM HeartRateSample").uniqueResult()).intValue();
  }

  /**
   * Get the number of HeartRateSamples stored in the database, which are flagged transmitted=false.
   *
   * @return int number of heart rate samples
   */
  @SuppressWarnings("deprecation")
  public int getNumberOfNotTransmittedHeartRateSamples() {
    LOG.info("get number of not transmitted heart rate samples");
    return ((Long) this.session.createQuery("SELECT count(*) FROM HeartRateSample WHERE transmitted=false").uniqueResult()).intValue();
  }

  /**
   * Delete all HeartRateSamples in the database. Does not reset the ID counter.
   *
   * @return int number of deleted samples
   */
  public int deleteAllHeartRateSamples() {
    LOG.info("delete all heart rate samples");
    return this.session.createQuery("DELETE FROM HeartRateSample").executeUpdate();
  }

  /**
   * Retrieve all PulseOximeterSamples stored in the database.
   *
   * @return List of PulseOximeterSamples
   */
  @SuppressWarnings("unchecked")
  public List<PulseOximeterSample> getPulseOximeterSamples() {
    LOG.info("get all pulse oximeter samples");
    return this.session.createQuery("FROM PulseOximeterSample").getResultList();
  }

  /**
   * Delete all PulseOximeterSamples in the database. Does not reset the ID counter.
   *
   * @return int number of deleted samples
   */
  public int deleteAllPulseOximeterSamples() {
    LOG.info("delete all pulse oximeter samples");
    return this.session.createQuery("DELETE FROM PulseOximeterSample").executeUpdate();
  }

  /**
   * Retrieve all CC2650TemperatureSamples stored in the database.
   *
   * @return List of CC2650TemperatureSamples
   */
  @SuppressWarnings("unchecked")
  public List<CC2650TemperatureSample> getCC2650TemperatureSamples() {
    LOG.info("get all CC2650 temperature samples");
    return this.session.createQuery("FROM CC2650TemperatureSample").getResultList();
  }

  /**
   * Retrieve all CC2650HumiditySamples stored in the database.
   *
   * @return List of CC2650HumiditySamples
   */
  @SuppressWarnings("unchecked")
  public List<CC2650HumiditySample> getCC2650HumiditySamples() {
    LOG.info("get all CC2650 humdity samples");
    return this.session.createQuery("FROM CC2650HumiditySample").getResultList();
  }

  /**
   * Retrieve all CC2650PressureSamples stored in the database.
   *
   * @return List of CC2650PressureSamples
   */
  @SuppressWarnings("unchecked")
  public List<CC2650PressureSample> getCC2650PressureSamples() {
    LOG.info("get all CC2650 pressure samples");
    return this.session.createQuery("FROM CC2650PressureSample").getResultList();
  }

  /**
   * Retrieve all CC2650TemperatureSamples stored in the database.
   *
   * @return List of CC2650AmbientlightSamples
   */
  @SuppressWarnings("unchecked")
  public List<CC2650AmbientlightSample> getCC2650AmbientlightSamples() {
    LOG.info("get all CC2650 ambientlight samples");
    return this.session.createQuery("FROM CC2650AmbientlightSample").getResultList();
  }

  /**
   * Retrieve all CC2650MovementSamples stored in the database.
   *
   * @return List of CC2650MovementSamples
   */
  @SuppressWarnings("unchecked")
  public List<CC2650MovementSample> getCC2650MovementSamples() {
    LOG.info("get all CC2650 movement samples");
    return this.session.createQuery("FROM CC2650MovementSample").getResultList();
  }

  /**
   * Retrieve all LuminoxTemperatureSamples stored in the database.
   *
   * @return List of CC2650TemperatureSamples
   */

  public List<LuminoxTemperatureSample> getLuminoxTemperatureSamples() {
    LOG.info("get all Luminox temperature samples");
    return this.session.createQuery("FROM LuminoxTemperatureSample").getResultList();
  }

  /**
   * Retrieve all LuminoxOxygenSamples stored in the database.
   *
   * @return List of CC2650TemperatureSamples
   */

  public List<LuminoxOxygenSample> getLuminoxOxygenSamples() {
    LOG.info("get all Luminox oxygen samples");
    return this.session.createQuery("FROM LuminoxOxygenSample").getResultList();
  }

  /**
   * Retrieve all LuminoxAirPressureSamples stored in the database.
   *
   * @return List of CC2650TemperatureSamples
   */

  public List<LuminoxAirPressureSample> getLuminoxAirPressureSamples() {
    LOG.info("get all Luminox temperature samples");
    return this.session.createQuery("FROM LuminoxAirPressureSample").getResultList();
  }
}
