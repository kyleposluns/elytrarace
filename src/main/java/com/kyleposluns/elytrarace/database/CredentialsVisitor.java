package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;

public interface CredentialsVisitor<R> {

  R visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials);

}
