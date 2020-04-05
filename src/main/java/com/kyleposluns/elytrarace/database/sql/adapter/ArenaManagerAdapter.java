package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.arena.ArenaImpl;
import com.kyleposluns.elytrarace.arena.ArenaInfo;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.arena.ArenaManagerImpl;
import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ArenaManagerAdapter implements SQLDeserializer<ArenaManager> {

  private static final String ARENA_ID = "arena_id";

  private static final String ARENA_NAME = "arena_name";

  private static final String DISPLAY_NAME = "display_name";

  private static final String WORLD_ID = "world_id";

  private static final String PITCH = "pitch";

  private static final String YAW = "yaw";

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  @Override
  public ArenaManager deserialize(Connection connection) {
    List<Arena> arenas = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareCall("{CALL get_arena_info()}")) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        UUID arenaId = UUID.fromString(rs.getString(ARENA_ID));
        String name = rs.getString(ARENA_NAME);
        String displayName = rs.getString(DISPLAY_NAME);
        UUID worldId = UUID.fromString(rs.getString(WORLD_ID));
        double x = rs.getDouble(X);
        double y = rs.getDouble(Y);
        double z = rs.getDouble(Z);
        float yaw = rs.getFloat(YAW);
        float pitch = rs.getFloat(PITCH);
        Location loc = new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
        List<Area> areas = new AreaAdapter(arenaId).deserialize(connection);
        ArenaInfo info = new ArenaInfo(worldId, arenaId, loc, name, displayName, areas);
        RecordBook records = new ArenaRecordBookDeserializer(arenaId).deserialize(connection);
        arenas.add(new ArenaImpl(info, records));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new ArenaManagerImpl(arenas);
  }
}
