package de.fhg.fit.biomos.sensorplatform.persistence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class for logging sensor values to a file.
 * 
 * @author Daniel Pyka
 *
 */
public class SampleLogger {

  private static final Logger LOG = LoggerFactory.getLogger(SampleLogger.class);

  private final Properties properties;
  private final SimpleDateFormat formatter;

  private PrintWriter pw = null;

  public SampleLogger(Properties properties, String measure, String sensorName) {
    this.properties = properties;
    this.formatter = new SimpleDateFormat(properties.getProperty("ditg.webinterface.timestamp.format"));

    File file = new File(new File(new File(this.properties.getProperty("sensor.data.directory"), sensorName), measure), measure + ".txt");

    if (file.exists()) {
      file.delete();
    } else {
      file.getParentFile().mkdirs();
    }

    this.pw = null;
    try {
      this.pw = new PrintWriter(file, "UTF-8");
      LOG.info("using log file: " + file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Write a line to the log file. Adds timestamp to the value. Always flushes the stream to persist the data.<br>
   * TODO format is fucked up currently and it is difficult to further work with it. Think about structure
   *
   * @param value
   */
  public void write(String value) {
    String timestamp = this.formatter.format(Calendar.getInstance().getTime());
    this.pw.println(timestamp + " " + value);
    this.pw.flush();
    System.out.println(timestamp + " " + value); // extreme debugging
  }

  /**
   * Close the stream to the log file gracefully.
   */
  public void close() {
    this.pw.flush();
    this.pw.close();
  }

}
