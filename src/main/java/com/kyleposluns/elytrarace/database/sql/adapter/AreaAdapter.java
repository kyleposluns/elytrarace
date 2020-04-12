package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.AreaBuilder;
import com.kyleposluns.elytrarace.arena.area.AreaType;
import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class AreaAdapter implements SQLDeserializer<List<Area>> {

  private static final String AREA_TYPE = "area_type";

  private static final String CENTER = "center_id";

  private static final String RADIUS = "radius";

  private static final String ROT_X = "rot_x";

  private static final String ROT_Y = "rot_y";

  private static final String ROT_Z = "rot_z";

  private static final String POSITION_1 = "pos1_id";

  private static final String POSITION_2 = "pos2_id";

  private static final String ORDER = "checkpoint_order";

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

        AreaType type = AreaType.valueOf(rs.getString(AREA_TYPE));
        if (!rs.wasNull()) {
          builder.type(type);
        }

        int order = rs.getInt(ORDER);
        if (!rs.wasNull()) {
          builder.order(order);
        }

        int centerId = rs.getInt(CENTER);
        if (!rs.wasNull()) {
          builder.center(new VectorDeserializer(centerId).deserialize(connection));
        }

        double radius = rs.getDouble(RADIUS);
        if (!rs.wasNull()) {
          builder.radius(radius);
        }

        double rotX = rs.getDouble(ROT_X);
        if (!rs.wasNull()) {
          builder.rotationX(rotX);
        }

        double rotY = rs.getDouble(ROT_Y);
        if (!rs.wasNull()) {
          builder.rotationY(rotY);
        }
        double rotZ = rs.getDouble(ROT_Z);
        if (!rs.wasNull()) {
          builder.rotationX(rotZ);
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
    Collections.sort(areas);

    return Collections.unmodifiableList(areas);
  }
}
