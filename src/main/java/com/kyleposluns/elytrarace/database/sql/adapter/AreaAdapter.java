package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.AreaBuilder;
import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Axis;

public class AreaAdapter implements SQLDeserializer<List<Area>> {

  private static final String CENTER = "center_id";

  private static final String RADIUS = "radius";

  private static final String AXIS = "axis";

  private static final String POSITION_1 = "pos_1";

  private static final String POSITION_2 = "pos_2";

  private final UUID arenaId;

  public AreaAdapter(UUID arenaId) {
    this.arenaId = arenaId;
  }

  @Override
  public List<Area> deserialize(Connection connection) {
    List<Area> areas = new ArrayList<>();

    try (CallableStatement statement = connection.prepareCall("{CALL get_arena_areas(?)}")) {
      statement.setString(1, this.arenaId.toString());
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        AreaBuilder builder = new AreaBuilder();
        int centerId = rs.getInt(CENTER);
        if (!rs.wasNull()) {
          builder.center(new VectorDeserializer(centerId).deserialize(connection));
        }

        double radius = rs.getDouble(RADIUS);
        if (!rs.wasNull()) {
          builder.radius(radius);
        }

        String axis = rs.getString(AXIS);
        if (!rs.wasNull()) {
          builder.axis(Axis.valueOf(axis));
        }

        int pos1Id = rs.getInt(POSITION_1);
        if (!(rs.wasNull())) {
          builder.position1(new VectorDeserializer(pos1Id).deserialize(connection));
        }

        int pos2Id = rs.getInt(POSITION_2);
        if (!(rs.wasNull())) {
          builder.position2(new VectorDeserializer(pos2Id).deserialize(connection));
        }
        try {
          areas.add(builder.build());
        } catch (IllegalStateException e) {
          e.printStackTrace();
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return areas;
  }
}
