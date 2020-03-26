package com.kyleposluns.elytrarace.database.mongo;

import com.kyleposluns.elytrarace.database.AbstractCredentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;

public class ElytraMongoCredentials extends AbstractCredentials {

  public ElytraMongoCredentials(String host, int port, String dbName,
      String user, String password) {
    super(host, port, dbName, user, password);
  }

  public ElytraMongoCredentials(String dbName, String user, String password) {
    this("localhost", 27017, dbName, user, password);
  }


  @Override
  public <R> R visitCredentials(CredentialsVisitor<R> visitor) {
    return visitor.visitMongoDBCredentials(this);
  }
}
