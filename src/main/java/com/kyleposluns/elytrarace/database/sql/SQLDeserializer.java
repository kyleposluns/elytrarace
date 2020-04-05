package com.kyleposluns.elytrarace.database.sql;

import java.sql.Connection;

/**
 * Represents a function that can read an object from an SQL database.
 * @param <R> The type of object that is to be read.
 */
public interface SQLDeserializer<R> {

  /**
   * Reads an object from an SQL database.
   * @param connection The connection to the database.
   * @return The object.
   */
  R deserialize(Connection connection);

}
