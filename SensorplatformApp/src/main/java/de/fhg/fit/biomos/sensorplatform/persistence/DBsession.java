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
    return this.session.createQuery("FROM HeartRateSample").getResultList();
  }

}
