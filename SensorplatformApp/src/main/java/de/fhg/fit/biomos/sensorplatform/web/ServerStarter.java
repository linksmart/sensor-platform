package de.fhg.fit.biomos.sensorplatform.web;

import java.util.Properties;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
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
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;

import de.fhg.fit.biomos.sensorplatform.guice.SensorplatformServletConfig;

/**
 *
 * @author Daniel Pyka
 *
 */
public class ServerStarter {

  private static final Logger LOG = LoggerFactory.getLogger(ServerStarter.class);

  private final Properties properties;

  private final SensorplatformServletConfig sensorplatformServletConfig;

  public ServerStarter(Properties properties, SensorplatformServletConfig sensorplatformServletConfig) {
    this.properties = properties;
    this.sensorplatformServletConfig = sensorplatformServletConfig;
  }

  public void start() {
    int port = Integer.parseInt(this.properties.getProperty("webapp.port"));
    Server server = new Server();

    HttpConfiguration https = new HttpConfiguration();
    https.addCustomizer(new SecureRequestCustomizer());
    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(ClassLoader.getSystemResource(this.properties.getProperty("keystore.filename")).toExternalForm());
    sslContextFactory.setKeyStorePassword(this.properties.getProperty("keystore.password"));
    sslContextFactory.setKeyManagerPassword(this.properties.getProperty("keystore.password"));
    ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
    sslConnector.setPort(port);
    // TODO bind connectors to eth0 and wlan0 but no other interface (especially not ppp)
    // or configure iptables to prevent access from "outside"

    server.setConnectors(new ServerConnector[] { sslConnector });

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.setSessionHandler(new SessionHandler(new HashSessionManager()));
    context.addEventListener(this.sensorplatformServletConfig);
    context.addFilter(GuiceFilter.class, "/*", null);
    context.addServlet(DefaultServlet.class, "/*");

    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setResourceBase("staticResources");
    resourceHandler.setMinMemoryMappedContentLength(-1);
    resourceHandler.setWelcomeFiles(new String[] { "index.html" });

    HashLoginService hls = new HashLoginService();
    hls.putUser(this.properties.getProperty("webapp.username"), Credential.getCredential(this.properties.getProperty("webapp.password")),
        new String[] { "default" });
    hls.setName("Sensorplatform");

    Constraint constraint = new Constraint();
    constraint.setName(Constraint.__BASIC_AUTH);
    constraint.setAuthenticate(true);
    constraint.setRoles(new String[] { "default" });

    ConstraintMapping cm = new ConstraintMapping();
    cm.setPathSpec("/*");
    cm.setConstraint(constraint);

    ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
    csh.setAuthenticator(new BasicAuthenticator());
    csh.setRealmName("Sensorplattform");
    csh.addConstraintMapping(cm);
    csh.setLoginService(hls);

    server.addBean(hls);

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resourceHandler, context });
    csh.setHandler(handlers);
    server.setHandler(csh);

    try {
      server.start();
    } catch (Exception e) {
      ServerStarter.LOG.error("Could not start the server:", e);
      System.exit(16);
    }
  }

}
