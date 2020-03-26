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
import org.bson.BsonBinary;
import org.bson.Document;

public class ElytraMongoDatabase extends AbstractDatabase {

  private static final String ARENAS_COLLECTION = "arenas";

  private static final String RECORD_BOOKS_COLLECTION = "records";

  private static final String CHECK_POINTS_COLLECTION = "check-points";

  private final MongoDatabase database;

  private final MongoClient client;

  public ElytraMongoDatabase(Credentials credentials) {
    super();
    this.client = credentials.visitCredentials(new MongoClientCreator());
    this.database = credentials.visitCredentials(new GetAuthenticatedDatabase(this.client));
  }

  @Override
  public List<Arena> getArenas() {
    MongoCollection<Document> collection = database.getCollection("arenas");
    return null;
  }

  private RecordBook findRecordBook(String name, UUID id) {
    FindIterable<Document> documents = database.getCollection(RECORD_BOOKS_COLLECTION)
        .find(new BasicDBObject(name, new BsonBinary(id).asDocument()));
    RecordBookBuilder builder = new RecordBookBuilder();

    StreamSupport.stream(documents.spliterator(), false)
        .map(Document::toJson).map(json -> gson.fromJson(json, Record.class))
        .forEach(builder::record);

    return builder.build();
  }

  @Override
  protected RecordBook findPlayerRecordBook(UUID playerId) {
    return findRecordBook("player-id", playerId);
  }

  @Override
  protected RecordBook findArenaRecordBook(UUID arenaId) {
    return findRecordBook("arena-id", arenaId);
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
