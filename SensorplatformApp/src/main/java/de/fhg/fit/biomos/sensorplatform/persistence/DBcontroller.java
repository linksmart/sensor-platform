package de.fhg.fit.biomos.sensorplatform.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that. Creates DBsession for database interactions.
 *
 * @author Daniel Pyka
 *
 */
public class DBcontroller {

  private static final Logger LOG = LoggerFactory.getLogger(DBcontroller.class);

  private final SessionFactory sessionFactory;

  public DBcontroller() {
    this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    LOG.info("connected to database");
  }

  /**
   * Any database interaction is performed via DBsession object. No direct interaction with SessionFactory allowed.
   *
   * @return DBsession for database interaction
   */
  public DBsession getSession() {
    return new DBsession(this.sessionFactory.openSession());
  }

  /**
   * Close the SessionFactory. Only used in the test.
   */
  public void close() {
    this.sessionFactory.close();
    LOG.info("disconnected from database");
  }

}
