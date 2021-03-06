package de.fhg.fit.biomos.sensorplatform.guice;

import java.util.Properties;

import de.fhg.fit.biomos.sensorplatform.control.*;
import de.fhg.fit.biomos.sensorplatform.system.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

import de.fhg.fit.biomos.sensorplatform.persistence.DBcontroller;
import de.fhg.fit.biomos.sensorplatform.restservices.CC2650Service;
import de.fhg.fit.biomos.sensorplatform.restservices.ControllerService;
import de.fhg.fit.biomos.sensorplatform.restservices.HeartRateService;
import de.fhg.fit.biomos.sensorplatform.restservices.PulseOximeterService;
import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;
import de.fhg.fit.biomos.sensorplatform.web.Uploader;
import de.fhg.fit.biomos.sensorplatform.web.WebHrsUploader;

/**
 * 1. Make properties (loaded from a file) available to all classes for which dependency injection is used.<br>
 * 2. Bind specific classes to interface classes dynamically (based on project configuration).<br>
 * 3. Bind core functionality classes as Singleton.
 *
 * @author Daniel Pyka
 *
 */
public class SensorplatformGuiceModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(SensorplatformGuiceModule.class);

  private final Properties properties;

  /**
   *
   * @param properties
   *          loaded from the file
   */
  public SensorplatformGuiceModule(Properties properties) {
    this.properties = properties;
  }

  @Override
  protected void configure() {
    bind(ControllerService.class);
    bind(HeartRateService.class);
    bind(PulseOximeterService.class);
    bind(CC2650Service.class);

    String webinterfaceName = this.properties.getProperty("webinterface.name");
    String targetPlatform = this.properties.getProperty("target.platform");
    String surfstickDevice = this.properties.getProperty("surfstick.device");
    
    switch (webinterfaceName) {
      case "TeLiPro":
        LOG.info("webinterface is {}", webinterfaceName);
        bind(TeLiProUploader.class).in(Singleton.class);
        bind(Uploader.class).to(TeLiProUploader.class);
        break;
      case "WebHrs":
        LOG.info("webinterface is {}", webinterfaceName);
        bind(WebHrsUploader.class).in(Singleton.class);
        bind(Uploader.class).to(WebHrsUploader.class);
        break;
      case "${webinterface.name}":
        LOG.warn("No webinterface maven profile specified");
        bind(Uploader.class).toProvider(Providers.of(null));
        break;
      default:
        LOG.error("unknown webinterface {}", webinterfaceName);
        bind(Uploader.class).toProvider(Providers.of(null));
        break;
    }

    switch (targetPlatform) {
      case "raspberrypi3":
        LOG.info("target platform is {}", targetPlatform);
        bind(RaspberryPi3.class).in(Singleton.class);
        bind(HardwarePlatform.class).to(RaspberryPi3.class);
        break;
      case "cubieboard3":
        LOG.info("target platform is {}", targetPlatform);
        bind(Cubieboard3.class).in(Singleton.class);
        bind(HardwarePlatform.class).to(Cubieboard3.class);
        break;
      default:
        LOG.error("unknown target platform {}", targetPlatform);
        System.exit(-1);
        break;
    }

    switch (surfstickDevice) {
      case "huaweie352s5":
        LOG.info("surfstick device is {}", surfstickDevice);
        bind(Huawei_E352S_5.class).in(Singleton.class);
        bind(Surfstick.class).to(Huawei_E352S_5.class);
        break;
      case "surfstickcol":
        LOG.info("surfstick device is {}", surfstickDevice);
        bind(SurfstickCol.class).in(Singleton.class);
        bind(Surfstick.class).to(SurfstickCol.class);
        break;
      default:
        LOG.error("unknown surfstick device {}", surfstickDevice);
        LOG.info("using default surfstick huaweie352s5");
        bind(Huawei_E352S_5.class).in(Singleton.class);
        bind(Surfstick.class).to(Huawei_E352S_5.class);
        break;
    }

    bind(HeartRateSampleCollector.class).in(Singleton.class);
    bind(PulseOximeterSampleCollector.class).in(Singleton.class);
    bind(CC2650SampleCollector.class).in(Singleton.class);
    bind(LuminoxSampleCollector.class).in(Singleton.class);
    bind(SensorWrapperFactory.class).in(Singleton.class);
    bind(Controller.class).in(Singleton.class);
    bind(DBcontroller.class).in(Singleton.class);
    try {
      Names.bindProperties(binder(), this.properties);
    } catch (Exception e) {
      LOG.error("Could not load properties", e);
    }
  }

}
