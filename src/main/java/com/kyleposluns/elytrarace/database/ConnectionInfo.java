package com.kyleposluns.elytrarace.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionInfo {

  private final String serverName;

  private final String port;

  private final String databaseName;

  private final String userName;

  private final String password;

  public ConnectionInfo(String serverName, String port, String databaseName, String userName,
      String password) {
    this.serverName = serverName;
    this.port = port;
    this.databaseName = databaseName;
    this.userName = userName;
    this.password = password;
  }

  public Connection createConnection() {
    String url =
        String.format("jdbc:mysql://%s:%s/%s", this.serverName, this.port, this.databaseName);
    Properties credentials = new Properties();
    credentials.put("user", this.userName);
    credentials.put("password", this.password);

    try {
      return DriverManager.getConnection(url, credentials);
    } catch (SQLException exception) {
      throw new IllegalArgumentException("Could not establish a connection.", exception);
    }
  }
}
