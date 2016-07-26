package de.fhg.fit.biomos.sensorplatform.guice;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class SensorplatformServletConfig extends GuiceServletContextListener {

  private Injector injector;
  private final Properties properties;

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
    return this.injector;
  }

  public Injector getCreatedInjector() {
    return this.injector;
  }

}
