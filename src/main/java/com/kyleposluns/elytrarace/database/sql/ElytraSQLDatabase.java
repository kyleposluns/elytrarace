package com.kyleposluns.elytrarace.database.sql;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.CredentialsVisitor;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.database.sql.adapter.ArenaManagerAdapter;
import com.kyleposluns.elytrarace.database.sql.adapter.PlayerRecordBookDeserializer;
import com.kyleposluns.elytrarace.database.sql.adapter.RecordBookSerializer;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.Plugin;

public class ElytraSQLDatabase implements ElytraDatabase {

  private final Plugin plugin;

  private final Connection connection;

  private final MessageFormatter messageFormatter;

  private Map<RecordBook, Long> lastRecordBookSave;

  public ElytraSQLDatabase(Plugin plugin, MessageFormatter messageFormatter,
      Credentials credentials) {
    this.plugin = plugin;
    this.messageFormatter = messageFormatter;
    this.lastRecordBookSave = new ConcurrentHashMap<>();
    this.connection = credentials.visitCredentials(new CreateDriverVisitor());
  }

  @Override
  public ArenaManager getArenaManager() {
    return new ArenaManagerAdapter(this.plugin, this.messageFormatter, this)
        .deserialize(this.connection);
  }

  @Override
  public void saveRecordBook(RecordBook recordBook) {
    long last;
    if (this.lastRecordBookSave.containsKey(recordBook)) {
      last = this.lastRecordBookSave.get(recordBook);
    } else {
      last = -1;
    }

    new RecordBookSerializer(last).serialize(this.connection, recordBook);
    this.lastRecordBookSave.put(recordBook, System.currentTimeMillis());
  }

  @Override
  public RecordBook getPlayerRecords(UUID playerId) {
    return new PlayerRecordBookDeserializer(playerId).deserialize(this.connection);
  }

  @Override
  public void close() {
    try {
      this.connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static class CreateDriverVisitor implements CredentialsVisitor<Connection> {

    @Override
    public Connection visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials) {
      try {
        System.out.println(String
            .format("jdbc:mysql://%s:%d/%s?serverTimezone=UTC", sqlCredentials.getHostName(),
                sqlCredentials.getPort(),
                sqlCredentials.getDatabaseName()));
        return DriverManager.getConnection(String
                .format("jdbc:mysql://%s:%d/%s?serverTimezone=UTC", sqlCredentials.getHostName(),
                    sqlCredentials.getPort(),
                    sqlCredentials.getDatabaseName()), sqlCredentials.getUser(),
            sqlCredentials.getPassword());
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    }
  }
}
