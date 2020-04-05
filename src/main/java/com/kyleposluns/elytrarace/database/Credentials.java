package com.kyleposluns.elytrarace.database;

/**
 * Represents credentials to a database.
 */
public interface Credentials {

  /**
   * Visits children of this object and performs a computation.
   * @param visitor The visitor object.
   * @param <R> The type of the result of the computation.
   * @return A result of a computation.
   */
  <R> R visitCredentials(CredentialsVisitor<R> visitor);

}
