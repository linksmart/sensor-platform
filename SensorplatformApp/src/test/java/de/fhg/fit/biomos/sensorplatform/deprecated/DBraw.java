package de.fhg.fit.biomos.sensorplatform.deprecated;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Deprecated
public class DBraw {

  // private static final Logger LOG = LoggerFactory.getLogger(DBraw.class);

  private final Connection conn;

  public static void main(String[] args) throws SQLException {
    DBraw x = new DBraw();
    x.test2();
    x.test();
    x.shutdown();
  }

  public DBraw() throws SQLException {
    this.conn = DriverManager.getConnection("jdbc:hsqldb:file:db" + File.separator + "sensorplatform", "sensorplatform", "123456");
    this.conn.setAutoCommit(false);
  }

  public void test() throws SQLException {
    Statement statement = this.conn.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM PulseOximeterSample WHERE id >= 0");
    while (resultSet.next()) {
      System.out.println(resultSet.getString("pulserate"));
    }
  }

  public void test2() throws SQLException {
    // String cmd = "INSERT INTO PULSEOXIMETERSAMPLE (firstname, lastname, timestamp, device, spo2, pulserate)
    // VALUES('Hans','Nutzlos','2016-09-27T10:10:10.010Z','11:22:33:44:55:66',92,180)";
    String insertPos = "INSERT INTO PULSEOXIMETERSAMPLE (firstname, lastname, timestamp, device, spo2, pulserate) VALUES(?,?,?,?,?,?)";
    PreparedStatement ps = this.conn.prepareStatement(insertPos);
    ps.setString(1, "PStest");
    ps.setString(2, "PStest2");
    ps.setString(3, "2016-09-27T10:26:10.010Z");
    ps.setString(4, "11:22:33:44:55:66");
    ps.setInt(5, 90);
    ps.setInt(6, 55);
    ps.executeUpdate();
    this.conn.commit();
  }

  public void shutdown() throws SQLException {
    this.conn.createStatement().executeQuery("SHUTDOWN");
  }
}
