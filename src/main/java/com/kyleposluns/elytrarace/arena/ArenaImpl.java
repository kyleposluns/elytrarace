package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import com.kyleposluns.elytrarace.tracking.RaceTrackerImpl;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class ArenaImpl implements Arena {

  private final UUID arenaId;

  private final UUID worldId;

  private final Location spawn;

  private final RecordBook recordBook;

  private final String name;

  private final List<Area> checkpoints;

  public ArenaImpl(UUID arenaId, UUID worldId, String name, Location spawn,
      List<Area> checkpoints, RecordBook recordBook) {
    this.arenaId = arenaId;
    this.worldId = worldId;
    this.spawn = spawn;
    this.name = name;
    this.recordBook = recordBook;
    this.checkpoints = checkpoints;
  }

  @Override
  public UUID getArenaId() {
    return this.arenaId;
  }

  @Override
  public UUID getWorldId() {
    return this.worldId;
  }

  @Override
  public Location getSpawn() {
    return this.spawn;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public RaceTracker createRaceTracker(Plugin plugin) {
    return new RaceTrackerImpl(plugin, this.arenaId, this.checkpoints, this.recordBook, this.spawn);
  }

  @Override
  public RecordBook getRecordBook() {
    return this.recordBook;
  }
}
