package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBookBuilder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public abstract class RecordBookDeserializer implements SQLDeserializer<RecordBook> {

  protected static final String ARENA_ID = "arena_id";

  protected static final String PLAYER_ID = "player_id";

  private static final String RECORD_ID = "record_id";

  private static final String DATE = "date_millis";

  private static final String SECONDS = "seconds";

  private final UUID id;

  public RecordBookDeserializer(UUID id) {
    this.id = id;
  }

  protected abstract String getQueryProcedure();

  @Override
  public RecordBook deserialize(Connection connection) {

    RecordBookBuilder builder = new RecordBookBuilder();
    try (CallableStatement statement = connection
        .prepareCall(String.format("{CALL %s(?)}", this.getQueryProcedure()))) {
      statement.setString(1, this.id.toString());
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        int recordId = rs.getInt(RECORD_ID);
        UUID arenaId = UUID.fromString(rs.getString(ARENA_ID));
        UUID playerId = UUID.fromString(rs.getString(PLAYER_ID));
        long date = rs.getLong(DATE);
        int seconds = rs.getInt(SECONDS);
        List<Vector> positionList = new PlayerPositionAdapter(recordId).deserialize(connection);
        builder.record(playerId, arenaId, date, seconds, positionList);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return builder.build();
  }

}
