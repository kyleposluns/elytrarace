package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import com.kyleposluns.elytrarace.tracking.RaceTrackerImpl;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ArenaImpl implements Arena {

  private final RaceTracker raceTracker;

  private final UUID arenaId;

  private final UUID worldId;

  private final Location spawn;

  private final RecordBook recordBook;

  private final String name;

  private final String displayName;

  public ArenaImpl(Plugin plugin, MessageFormatter messageFormatter, ElytraDatabase database,
      ArenaInfo info, RecordBook recordBook, List<Vector> path) {
    this.arenaId = info.getArenaId();
    this.worldId = info.getWorldId();
    this.spawn = info.getSpawn();
    this.recordBook = recordBook;
    List<Area> checkpoints = info.getAreas();
    this.name = info.getName();
    this.displayName = info.getDisplayName();
    this.raceTracker = new RaceTrackerImpl(plugin, messageFormatter, database, this.arenaId,
        checkpoints, path,
        this.recordBook,
        this.spawn);
    plugin.getServer().getPluginManager().registerEvents(this.raceTracker, plugin);
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
  public String getDisplayName() {
    return this.displayName;
  }

  @Override
  public RaceTracker getRaceTracker() {
    return this.raceTracker;
  }

  @Override
  public RecordBook getRecordBook() {
    return this.recordBook;
  }

}
