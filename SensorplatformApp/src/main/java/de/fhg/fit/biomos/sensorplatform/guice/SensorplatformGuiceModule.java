package de.fhg.fit.biomos.sensorplatform.guice;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.fhg.fit.biomos.sensorplatform.control.Controller;
import de.fhg.fit.biomos.sensorplatform.control.SensorObserver;
import de.fhg.fit.biomos.sensorplatform.control.SensorWrapperFactory;
import de.fhg.fit.biomos.sensorplatform.main.ShellscriptExecutor;
import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.restservices.HeartRateService;
import de.fhg.fit.biomos.sensorplatform.restservices.InfoService;
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
    bind(InfoService.class);
    bind(HeartRateService.class);

    // TODO make this part dynamic to allow different webinterfaces
    bind(TeLiProUploader.class).in(Singleton.class);
    //
    bind(SensorObserver.class).in(Singleton.class);
    bind(SensorWrapperFactory.class).in(Singleton.class);
    bind(Controller.class).in(Singleton.class);
    bind(DBcontroller.class).in(Singleton.class);
    bind(ShellscriptExecutor.class).in(Singleton.class);

    try {
      Names.bindProperties(binder(), this.properties);
    } catch (Exception e) {
      LOG.error("Could not load properties", e);
    }
  }

}
