package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.database.sql.SQLSerializer;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class RecordBookSerializer implements SQLSerializer<RecordBook> {

  @Override
  public void serialize(Connection connection, RecordBook recordBook) {
    try {
      connection.setAutoCommit(false);
      for (Record record : recordBook) {
        new RecordAdapter().serialize(connection, record);
      }
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      try {
        connection.rollback();
        connection.setAutoCommit(true);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  static class RecordAdapter implements SQLSerializer<Record> {

    @Override
    public void serialize(Connection connection, Record object) {
      try (CallableStatement recordInfo = connection
          .prepareCall("{CALL add_record(?, ?, ?, ?, ?)}")) {
        recordInfo.setString(1, object.getPlayerId().toString());
        recordInfo.setString(2, object.getArenaId().toString());
        recordInfo.setLong(3, object.getDate());
        recordInfo.setInt(4, object.getTime());
        recordInfo.registerOutParameter(5, Types.INTEGER);
        recordInfo.execute();
        int recordId = recordInfo.getInt(5);
        new PlayerPositionAdapter(recordId).serialize(connection, object.getPositions());
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }
  }
}
