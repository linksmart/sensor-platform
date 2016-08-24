package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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

  public void writeLine(String line) {
    this.pw.println(line);
    this.pw.flush();
  }

  public void close() {
    LOG.info("closing file logger");
    this.pw.flush();
    this.pw.close();
  }

}
