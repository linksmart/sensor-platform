package de.fhg.fit.biomos.sensorplatform.web;

import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;

import de.fhg.biomos.sensorplatform.guice.SensorplatformServletConfig;

public class ServerStarter implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(ServerStarter.class);

  private final Properties properties;

  public ServerStarter(Properties properties) {
    this.properties = properties;
  }

  @Override
  public void run() {
    int port = Integer.parseInt(this.properties.getProperty("webinterface.port"));
    Server server = new Server(port);

    SensorplatformServletConfig sensorplatformServletConfig = new SensorplatformServletConfig(this.properties);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.setSessionHandler(new SessionHandler(new HashSessionManager()));
    context.addEventListener(sensorplatformServletConfig);
    context.addFilter(GuiceFilter.class, "/*", null);
    context.addServlet(DefaultServlet.class, "/*");

    // ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
    // jerseyServlet.setInitOrder(0);
    // jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages", "de.fhg.fit.biomos.sensorplatform.restservices");

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setResourceBase("staticResources");
    resourceHandler.setMinMemoryMappedContentLength(-1);
    resourceHandler.setWelcomeFiles(new String[] { "index.html" });

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resourceHandler, context });
    server.setHandler(handlers);

    try {
      server.start();
    } catch (Exception e) {
      ServerStarter.LOG.error("Could not start the server:", e);
      System.exit(16);
    }

  }

}
