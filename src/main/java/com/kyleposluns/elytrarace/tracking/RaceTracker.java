package com.kyleposluns.elytrarace.tracking;


import java.util.UUID;
import org.bukkit.event.Listener;

/**
 * Object responsible for tracking a player during their run at the race.
 */
public interface RaceTracker extends Listener {

  /**
   * Determines if a given player is actively flying in the course. If this is true, it also implies
   * that isTracked is also true.
   *
   * @param playerId The ID of the player.
   * @return Whether or not the player is racing.
   */
  boolean isRacing(UUID playerId);

  /**
   * Determines if a player is being tracked by this tracker. This does not imply that
   * the player is currently racing.
   */
  boolean isTracked(UUID playerId);

  /**
   * Gets the current runtime of the player's run if the player is not currently racing this will
   * throw an IllegalArgumentException.
   *
   * @param playerId The ID of the player.
   * @return The time of the player.
   * @throws IllegalArgumentException if the player is not currently racing.
   */
  long getCurrentTime(UUID playerId) throws IllegalArgumentException;

}
