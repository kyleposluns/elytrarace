package com.kyleposluns.elytrarace.database;

import java.sql.Connection;

public class ElytraDatabase {

  private Connection databaseConnection;

  public ElytraDatabase(ConnectionInfo connectionInfo) {
    this.databaseConnection = connectionInfo.createConnection();
  }



}
