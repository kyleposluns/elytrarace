package com.kyleposluns.elytrarace.database;

public abstract class AbstractCredentials implements Credentials {

  protected final String hostName;

  protected final int port;

  protected final String databaseName;

  protected final String user;

  protected final String password;

  public AbstractCredentials(String hostName, int port, String databaseName, String user,
      String password) {
    this.hostName = hostName;
    this.port = port;
    this.databaseName = databaseName;
    this.user = user;
    this.password = password;
  }

  public String getHostName() {
    return this.hostName;
  }

  public int getPort() {
    return this.port;
  }

  public String getDatabaseName() {
    return this.databaseName;
  }

  public String getUser() {
    return this.user;
  }

  public String getPassword() {
    return this.password;
  }

}
