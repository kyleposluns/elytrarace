package com.kyleposluns.elytrarace.game;

import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RaceCoordinatorImpl implements RaceCoordinator {

  private final Map<UUID, String> playerArenaMap;

  private final ArenaManager arenaManager;

  public RaceCoordinatorImpl(ArenaManager arenaManager) {
    this.arenaManager = arenaManager;
    this.playerArenaMap = new HashMap<>();
  }

  @Override
  public void beginTracking(UUID playerId, String arenaName) {
    if (this.playerArenaMap.containsKey(playerId)) {
      this.stopTracking(playerId);
    }

    if (!this.arenaManager.hasArena(arenaName)) {
      throw new IllegalArgumentException(
          String.format("Cannot find an arena by the name %s", arenaName));
    }


    this.arenaManager.getRaceTracker(arenaName).track(playerId);
    this.playerArenaMap.put(playerId, arenaName);
    Arena arena = this.arenaManager.getArena(arenaName);
    Player player = Bukkit.getPlayer(playerId);
    if (player != null) {
      player.teleport(arena.getSpawn());
    }

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
    return this.arenaManager.getRaceTracker(this.playerArenaMap.get(playerId)).getCurrentTime(playerId);
  }

  @Override
  public void stopTracking(UUID playerId) {
    if (this.playerArenaMap.containsKey(playerId)) {
      this.arenaManager.getRaceTracker(this.playerArenaMap.get(playerId)).unTrack(playerId);
      this.playerArenaMap.remove(playerId);
    }
  }

  @Override
  public void untrackAll() {
    this.playerArenaMap.keySet().forEach(this::stopTracking);
  }
}
