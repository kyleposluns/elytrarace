package com.kyleposluns.elytrarace.database;


import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLDatabase;

public class GetDatabaseVisitor implements CredentialsVisitor<ElytraDatabase> {

  @Override
  public ElytraDatabase visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials) {
    return new ElytraSQLDatabase(sqlCredentials);
  }
}
