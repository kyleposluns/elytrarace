package com.kyleposluns.elytrarace.game;

import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.plugin.Plugin;

public class RaceCoordinatorImpl implements RaceCoordinator {

  private final Plugin plugin;

  private final Map<UUID, String> playerArenaMap;

  private final ElytraDatabase database;

  private final ArenaManager arenaManager;

  public RaceCoordinatorImpl(Plugin plugin, ElytraDatabase database, ArenaManager arenaManager) {
    this.plugin = plugin;
    this.database = database;
    this.arenaManager = arenaManager;
    this.playerArenaMap = new HashMap<>();
  }

  private RaceTracker getOrCreateRaceTracker(String arenaName) {
    if (this.arenaManager.hasArena(arenaName)) {
      Arena arena = this.arenaManager.getArena(arenaName);

      return arena.createRaceTracker(this.plugin, this.database);
    } else {
      throw new IllegalArgumentException(
          String.format("Cannot find an arena by the name %s", arenaName));
    }
  }

  @Override
  public void beginTracking(UUID playerId, String arenaName) {
    if (this.playerArenaMap.containsKey(playerId)) {
      this.stopTracking(playerId);
    }
    this.getOrCreateRaceTracker(arenaName).track(playerId);
    this.playerArenaMap.put(playerId, arenaName);
  }

  @Override
  public Arena getCurrentArena(UUID playerId) throws IllegalArgumentException {
    if (!this.playerArenaMap.containsKey(playerId)) {
      throw new IllegalArgumentException(
          String.format("The player %s is not being tracked.", playerId.toString()));
    }

    return arenaManager.getArena(this.playerArenaMap.get(playerId));
  }

  @Override
  public long getCurrentTime(UUID playerId) throws IllegalArgumentException {
    if (!this.playerArenaMap.containsKey(playerId)) {
      throw new IllegalArgumentException(
          String.format("The player %s is not being tracked.", playerId.toString()));
    }
    return this.getOrCreateRaceTracker(this.playerArenaMap.get(playerId)).getCurrentTime(playerId);
  }

  @Override
  public void stopTracking(UUID playerId) {
    if (this.playerArenaMap.containsKey(playerId)) {
      this.getOrCreateRaceTracker(this.playerArenaMap.get(playerId)).unTrack(playerId);
      this.playerArenaMap.remove(playerId);
    }
  }

  @Override
  public void untrackAll() {
    this.playerArenaMap.keySet().forEach(this::stopTracking);
  }
}
