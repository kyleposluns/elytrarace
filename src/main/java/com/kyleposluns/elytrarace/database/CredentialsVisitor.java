package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;

public interface CredentialsVisitor<R> {

  R visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials);

  R visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials);

}
