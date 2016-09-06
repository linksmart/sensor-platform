package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Deprecated
public class TimeTest {
  @Deprecated
  public static void main(String[] args) throws InterruptedException {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
    sdf.setTimeZone(TimeZone.getTimeZone("Zulu"));
    System.out.println(sdf.format(Calendar.getInstance().getTime()));

    DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(DateTimeZone.UTC);
    System.out.println(dtf.print(new DateTime()));

  }

}
