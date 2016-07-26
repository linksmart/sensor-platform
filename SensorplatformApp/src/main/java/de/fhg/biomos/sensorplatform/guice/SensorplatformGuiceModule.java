package de.fhg.biomos.sensorplatform.guice;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.control.SensorWrapperFactory;
import de.fhg.fit.biomos.sensorplatform.restservices.StartupService;
import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;

public class SensorplatformGuiceModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(SensorplatformGuiceModule.class);

  private final Properties properties;

  public SensorplatformGuiceModule(Properties properties) {
    this.properties = properties;
  }

  @Override
  protected void configure() {
    bind(StartupService.class);

    // TODO make this part dynamic
    bind(TeLiProUploader.class).in(Singleton.class);
    bind(SensorWrapperFactory.class).in(Singleton.class);
    bind(Controller.class).in(Singleton.class);

    try {
      Names.bindProperties(binder(), this.properties);
    } catch (Exception e) {
      LOG.error("Could not load properties", e);
    }
  }

}
