package com.kyleposluns.elytrarace.database.sql;

import java.sql.Connection;

/**
 * Represents a function that add a java object to an SQL database.
 * @param <T> The type of object.
 */
public interface SQLSerializer<T> {

  /**
   * Adds a java object to an SQL database.
   * @param connection The connection to the database.
   * @param object The object being serialized.
   */
  void serialize(Connection connection, T object);

}
