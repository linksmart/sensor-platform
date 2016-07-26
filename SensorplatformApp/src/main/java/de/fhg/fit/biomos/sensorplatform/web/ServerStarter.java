package de.fhg.fit.biomos.sensorplatform.web;

import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
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
    int port = Integer.parseInt(this.properties.getProperty("sensorplatform.https.port"));
    Server server = new Server();

    HttpConfiguration https = new HttpConfiguration();
    https.addCustomizer(new SecureRequestCustomizer());
    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(ClassLoader.getSystemResource(this.properties.getProperty("keystore.filename")).toExternalForm());
    sslContextFactory.setKeyStorePassword("123456");
    sslContextFactory.setKeyManagerPassword("123456");
    ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
    sslConnector.setPort(port);
    // TODO bind connectors to eth0 and wlan0 but no other interface (especially not ppp)

    server.setConnectors(new ServerConnector[] { sslConnector });

    SensorplatformServletConfig sensorplatformServletConfig = new SensorplatformServletConfig(this.properties);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.setSessionHandler(new SessionHandler(new HashSessionManager()));
    context.addEventListener(sensorplatformServletConfig);
    context.addFilter(GuiceFilter.class, "/*", null);
    context.addServlet(DefaultServlet.class, "/*");

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
