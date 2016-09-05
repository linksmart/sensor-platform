package de.fhg.fit.biomos.webhrs.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 *
 * @author Daniel Pyka
 *
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private static final int PORT = 8080;

  public static void main(String[] args) {
    new Main().start();
  }

  private void start() {

    Server server = new Server(PORT);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);
    jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages", "de.fhg.fit.biomos.restservices");

    server.setHandler(context);

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      LOG.error("Could not start the server at port " + PORT, e);
      System.exit(16);
    } finally {
      server.destroy();
      System.exit(0);
    }

  }
}
