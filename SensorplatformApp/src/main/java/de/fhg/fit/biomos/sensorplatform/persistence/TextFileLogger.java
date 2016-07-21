package de.fhg.fit.biomos.sensorplatform.persistence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class for logging sensor values to a file.
 *
 * @author Daniel Pyka
 *
 */
public class TextFileLogger {

  private static final Logger LOG = LoggerFactory.getLogger(TextFileLogger.class);

  private PrintWriter pw = null;

  public TextFileLogger(String sensorName) {

    File file = new File(new File("data", sensorName), sensorName + ".txt");

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

  public void addDescriptionLine(String line) {
    this.pw.println("# " + line);
    this.pw.flush();
  }

  /**
   * Write a line to the log file. Adds timestamp to the value. Always flushes the stream to persist the data.<br>
   *
   * @param value
   */
  public void writeLine(String line) {
    this.pw.println(line);
    this.pw.flush();
  }

  /**
   * Close the stream to the log file gracefully.
   */
  public void close() {
    LOG.info("closing file logger");
    this.pw.flush();
    this.pw.close();
  }

}
