package com.kyleposluns.elytrarace.game;

import com.kyleposluns.elytrarace.map.Arena;
import java.util.UUID;
import org.bukkit.event.Listener;

/**
 * Responsible for managing which races players are in.
 */
public interface RaceCoordinator extends Listener {

  /**
   * Assign a player to an arena to be tracked by its RaceTracker.
   *
   * @param playerId  The id of the player.
   * @param arenaName The name of the arena.
   */
  void beginTracking(UUID playerId, String arenaName);

  /**
   * Find the current arena the player is in.
   *
   * @param playerId The player's id.
   * @return The player's current arena.
   * @throws IllegalArgumentException If the player isn't being tracked by any arena.
   */
  Arena getCurrentArena(UUID playerId) throws IllegalArgumentException;

  /**
   * Stops tracking the player no matter which arena they're in. This is a No-op if the player is
   * not being tracked.
   *
   * @param playerId The id of the player.
   */
  void stopTracking(UUID playerId);

}
