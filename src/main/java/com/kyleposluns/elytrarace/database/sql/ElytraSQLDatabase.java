package com.kyleposluns.elytrarace.database.sql;

import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.database.AbstractDatabase;
import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;
import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;
import com.kyleposluns.elytrarace.database.sql.adapter.ArenaManagerAdapter;
import com.kyleposluns.elytrarace.database.sql.adapter.ArenaRecordBookDeserializer;
import com.kyleposluns.elytrarace.database.sql.adapter.PlayerRecordBookDeserializer;
import com.kyleposluns.elytrarace.database.sql.adapter.RecordBookSerializer;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class ElytraSQLDatabase extends AbstractDatabase {

  private final Connection connection;

  public ElytraSQLDatabase(Credentials credentials) {
    super(credentials);
    this.connection = credentials.visitCredentials(new CreateDriverVisitor());
  }

  @Override
  protected RecordBook findPlayerRecordBook(UUID playerId) {
    return new PlayerRecordBookDeserializer(playerId).deserialize(this.connection);
  }

  @Override
  protected RecordBook findArenaRecordBook(UUID arenaId) {
    return new ArenaRecordBookDeserializer(arenaId).deserialize(this.connection);
  }

  @Override
  public ArenaManager getArenaManager() {
    return new ArenaManagerAdapter().deserialize(this.connection);
  }

  @Override
  public void saveRecordBook(RecordBook recordBook) {
    new RecordBookSerializer().serialize(this.connection, recordBook);
  }

  static class CreateDriverVisitor implements CredentialsVisitor<Connection> {

    @Override
    public Connection visitMongoDBCredentials(ElytraMongoCredentials mongoCredentials) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Connection visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials) {
      try {
        return DriverManager.getConnection(String
                .format("jdbc:mysql://%s:%d/%s", sqlCredentials.getHostName(), sqlCredentials.getPort(),
                    sqlCredentials.getDatabaseName()), sqlCredentials.getUser(),
            sqlCredentials.getPassword());
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
