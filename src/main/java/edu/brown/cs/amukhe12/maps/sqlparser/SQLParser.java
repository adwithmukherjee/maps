package edu.brown.cs.amukhe12.maps.sqlparser;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.maps.MapGraph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SQLParser {

  private static Connection conn = null;
  EntryList _list;

  public SQLParser(String filename, EntryList list) throws Exception {
    _list = list;
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn=DriverManager.getConnection(urlToDB);
  }

  public void parse(String statement) throws Exception {
    PreparedStatement prep = conn.prepareStatement(statement);
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      List<String> fields = new ArrayList<>();
      //= Arrays.asList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
      for(int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
        fields.add(rs.getString(i));
      }
      _list.addEntry(fields);

    }
    rs.close();
    prep.close();
  }

  public List<List<String>> parseAndReturnList(String statement) throws Exception{
    PreparedStatement prep = conn.prepareStatement(statement);
    ResultSet rs = prep.executeQuery();
    List<List<String>> table = new ArrayList<List<String>>();
    while (rs.next()) {
      List<String> fields = new ArrayList<>();
      //= Arrays.asList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
      for(int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
        fields.add(rs.getString(i));
      }
      table.add(fields);
    }
    rs.close();
    prep.close();
    return table;
  }

}
