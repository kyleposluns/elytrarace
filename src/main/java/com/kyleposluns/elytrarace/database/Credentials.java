package com.kyleposluns.elytrarace.database;

public interface Credentials {

  <R> R visitCredentials(CredentialsVisitor<R> visitor);

}
