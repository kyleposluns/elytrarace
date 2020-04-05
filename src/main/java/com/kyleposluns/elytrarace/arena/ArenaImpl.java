package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
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

  private final String displayName;

  private final List<Area> checkpoints;

  private transient RaceTracker tracker;

  public ArenaImpl(ArenaInfo info, RecordBook recordBook) {
    this.arenaId = info.getArenaId();
    this.worldId = info.getWorldId();
    this.spawn = info.getSpawn();
    this.recordBook = recordBook;
    this.checkpoints = info.getAreas();
    this.name = info.getName();
    this.displayName = info.getDisplayName();
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
  public RaceTracker createRaceTracker(Plugin plugin, ElytraDatabase database) {
    if (this.tracker == null) {
      this.tracker = new RaceTrackerImpl(plugin, database, this.arenaId, this.checkpoints,
          this.recordBook,
          this.spawn);
      plugin.getServer().getPluginManager().registerEvents(this.tracker, plugin);
    }
    return this.tracker;
  }

  @Override
  public RecordBook getRecordBook() {
    return this.recordBook;
  }

}
