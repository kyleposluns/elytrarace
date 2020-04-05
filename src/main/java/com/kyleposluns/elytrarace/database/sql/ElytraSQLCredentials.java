package com.kyleposluns.elytrarace.database.sql;

import com.kyleposluns.elytrarace.database.AbstractCredentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;

public class ElytraSQLCredentials extends AbstractCredentials {

  public ElytraSQLCredentials(String hostName, int port, String database, String user,
      String password) {
    super(hostName, port, database, user, password);
  }

  @Override
  public <R> R visitCredentials(CredentialsVisitor<R> visitor) {
    return visitor.visitSQLDBCredentials(this);
  }
}
