package de.fhg.fit.biomos.sensorplatform.deprecated;
// package de.fhg.fit.biomos.sensorplatform.main;
//
// import java.util.Properties;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import com.google.inject.AbstractModule;
// import com.google.inject.Guice;
// import com.google.inject.Injector;
// import com.google.inject.name.Names;
//
// public class SensorPlatformGuiceModule {
// private static final Logger LOG = LoggerFactory.getLogger(SensorPlatformGuiceModule.class);
//
// private final Injector injector;
//
// public SensorPlatformGuiceModule(Properties properties) {
// AbstractModule abstractModule = new AbstractModule() {
//
// @Override
// protected void configure() {
// bind();
// Names.bindProperties(binder(), properties);
// LOG.info("properties loaded");
//
// }
// };
// this.injector = Guice.createInjector(abstractModule);
// }
//
// public Injector get() {
// return this.injector;
// }
//
// }
