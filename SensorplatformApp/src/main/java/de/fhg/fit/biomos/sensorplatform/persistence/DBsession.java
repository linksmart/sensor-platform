package de.fhg.fit.biomos.sensorplatform.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fit.biomos.sensorplatform.sample.HeartRateSample;

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
      LOG.info("committed");
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

}
