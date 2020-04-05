package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;

/**
 * Visits a credentials object and performs a computation.
 *
 * See visitor pattern.
 * @param <R> The type of the result of the computation.
 */
public interface CredentialsVisitor<R> {

  /**
   * Visits mongo credentials and performs a computation on mongo credentials.
   * @param mongoCredentials The mongo credentials.
   * @return Some result.
   */
  R visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials);

  /**
   * Visits SQL credentials and performs a computation on SQL credentials.
   * @param sqlCredentials The sql credentials.
   * @return Some result.
   */
  R visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials);

}
