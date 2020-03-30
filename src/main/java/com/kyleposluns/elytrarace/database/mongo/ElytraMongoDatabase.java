package com.kyleposluns.elytrarace.database.mongo;

import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.database.AbstractDatabase;
import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Collections;
import java.util.UUID;

public class ElytraMongoDatabase extends AbstractDatabase {

  private static final String ARENAS_COLLECTION = "arenas";

  private static final String RECORD_BOOKS_COLLECTION = "records";

  private final MongoDatabase database;

  private final MongoClient client;

  public ElytraMongoDatabase(Credentials credentials) {
    super();
    this.client = credentials.visitCredentials(new MongoClientCreator());
    this.database = credentials.visitCredentials(new GetAuthenticatedDatabase(this.client));
    System.out.println(this.database.getName());
  }


  @Override
  protected RecordBook findPlayerRecordBook(UUID playerId) {
    return null;
  }

  @Override
  protected RecordBook findArenaRecordBook(UUID arenaId) {
    return null;
  }

  @Override
  public ArenaManager getArenaManager() {
    return null;
  }

  @Override
  public void saveRecordBook(RecordBook recordBook) {

  }


  static class GetAuthenticatedDatabase implements CredentialsVisitor<MongoDatabase> {

    private final MongoClient client;

    GetAuthenticatedDatabase(MongoClient client) {
      this.client = client;
    }

    @Override
    public MongoDatabase visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials) {
      return client.getDatabase(mongoCredentials.getDatabaseName());
    }
  }


  static class MongoClientCreator implements CredentialsVisitor<MongoClient> {

    @Override
    public MongoClient visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials) {

      MongoCredential credential = MongoCredential
          .createCredential(mongoCredentials.getUser(), mongoCredentials.getDatabaseName(),
              mongoCredentials.getPassword().toCharArray());
      return new MongoClient(
          new ServerAddress(mongoCredentials.getHostName(), mongoCredentials.getPort()),
          Collections.singletonList(credential));
    }
  }

}
