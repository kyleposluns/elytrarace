package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.arena.area.Area;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;

public class ArenaInfo {

  private final UUID worldId;

  private final UUID arenaId;

  private final Location spawn;

  private final String name;

  private final String displayName;

  private final List<Area> areas;

  public ArenaInfo(UUID worldId, UUID arenaId, Location spawn, String name, String displayName, List<Area> areas) {
    this.worldId = worldId;
    this.arenaId = arenaId;
    this.spawn = spawn;
    this.displayName = displayName;
    this.name = name;
    this.areas = areas;
  }

  public UUID getWorldId() {
    return this.worldId;
  }

  public List<Area> getAreas() {
    return areas;
  }

  public Location getSpawn() {
    return spawn;
  }

  public String getName() {
    return name;
  }

  public UUID getArenaId() {
    return arenaId;
  }

  public String getDisplayName() {
    return this.displayName;
  }
}
