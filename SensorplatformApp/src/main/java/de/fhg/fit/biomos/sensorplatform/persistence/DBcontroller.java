package de.fhg.fit.biomos.sensorplatform.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class <b>must</b> be used as a singleton. Configured with <b>GUICE</b> to enforce that.
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

  public DBsession getSession() {
    return new DBsession(this.sessionFactory.openSession());
  }

  public void close() {
    this.sessionFactory.close();
    LOG.info("disconnected from database");
  }

}
