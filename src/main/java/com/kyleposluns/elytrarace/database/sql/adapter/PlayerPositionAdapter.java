package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import com.kyleposluns.elytrarace.database.sql.SQLSerializer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.Vector;

public class PlayerPositionAdapter implements SQLDeserializer<List<Vector>>,
    SQLSerializer<List<Vector>> {

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  private final int recordId;

  public PlayerPositionAdapter(int recordId) {
    this.recordId = recordId;
  }

  @Override
  public List<Vector> deserialize(Connection connection) {
    List<Vector> positions = new ArrayList<>();

    try (CallableStatement statement = connection
        .prepareCall("{CALL get_player_positions(?)}")) {

      statement.setInt(1, this.recordId);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        double x = resultSet.getDouble(X);
        double y = resultSet.getDouble(Y);
        double z = resultSet.getDouble(Z);
        positions.add(new Vector(x, y, z));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return positions;
  }

  @Override
  public void serialize(Connection connection, List<Vector> vectors) {
    try {
      connection.setAutoCommit(false);

      for (Vector vector : vectors) {

        try (CallableStatement statement = connection
            .prepareCall("{CALL add_position_to_record(?, ?, ?, ?)}")) {
          statement.setInt(1, this.recordId);
          statement.setDouble(2, vector.getX());
          statement.setDouble(3, vector.getY());
          statement.setDouble(4, vector.getZ());
          statement.execute();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      connection.commit();
    } catch (SQLException exception) {
      try {
        connection.rollback();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
