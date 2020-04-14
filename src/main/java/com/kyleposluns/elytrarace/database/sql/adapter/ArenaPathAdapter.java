package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class ArenaPathAdapter implements SQLDeserializer<List<Vector>> {

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  private final UUID arenaId;

  public ArenaPathAdapter(UUID arenaId) {
    this.arenaId = arenaId;
  }

  @Override
  public List<Vector> deserialize(Connection connection) {
    List<Vector> positions = new ArrayList<>();

    try (CallableStatement statement = connection
        .prepareCall("{CALL get_arena_path(?)}")) {

      statement.setString(1, this.arenaId.toString());

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


}
