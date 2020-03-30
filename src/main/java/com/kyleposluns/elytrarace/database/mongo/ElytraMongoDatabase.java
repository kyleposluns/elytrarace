package com.kyleposluns.elytrarace.database.mongo;

import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.database.AbstractDatabase;
import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBookBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.print.Doc;
import org.bson.BsonBinary;
import org.bson.Document;

public class ElytraMongoDatabase extends AbstractDatabase {

  private static final String ARENAS_COLLECTION = "arenas";

  private static final String RECORD_BOOKS_COLLECTION = "records";

  private static final String CHECK_POINTS_COLLECTION = "checkpoints";

  private final MongoDatabase database;

  private final MongoClient client;

  public ElytraMongoDatabase(Credentials credentials) {
    super();
    this.client = credentials.visitCredentials(new MongoClientCreator());
    this.database = credentials.visitCredentials(new GetAuthenticatedDatabase(this.client));
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
  public List<Arena> getArenas() {
    return null;
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
      return new MongoClient(new ServerAddress(mongoCredentials.getHostName(), mongoCredentials.getPort()),
          Collections.singletonList(credential));
    }
  }

}
