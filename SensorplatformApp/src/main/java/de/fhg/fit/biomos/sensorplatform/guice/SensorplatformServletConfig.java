package de.fhg.fit.biomos.sensorplatform.guice;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import de.fhg.fit.biomos.sensorplatform.control.Controller;

/**
 * Servlet configuration for Guice used with Jetty. Everytime a REST call arrives under a defined URL, a new object is created, injected and thrown away by the
 * carbage collector after processing the response.
 *
 * @author Daniel Pyka
 *
 */
public class SensorplatformServletConfig extends GuiceServletContextListener {

  private Injector injector;
  private final Properties properties;

  /**
   *
   * @param properties
   *          Properties loaded from the file
   */
  public SensorplatformServletConfig(Properties properties) {
    this.properties = properties;
  }

  @Override
  protected Injector getInjector() {
    JerseyServletModule jerseyServletModule = new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        install(new SensorplatformGuiceModule(SensorplatformServletConfig.this.properties));
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages", "de.fhg.fit.biomos.sensorplatform.restservices");
        serve("/*").with(GuiceContainer.class, initParams);
      }
    };
    this.injector = Guice.createInjector(jerseyServletModule);
    initialiseController();
    return this.injector;
  }

  /**
   * Explicitly initialise the controller at startup. This will check the last state of the sensorplatform. In parallel, the web server is started.
   */
  private void initialiseController() {
    Controller controller = this.injector.getInstance(Controller.class);
    controller.initialise();
  }

}
