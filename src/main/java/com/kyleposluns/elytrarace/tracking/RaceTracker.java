package com.kyleposluns.elytrarace.tracking;


import java.util.UUID;
import org.bukkit.event.Listener;

/**
 * Object responsible for tracking a player during their run at the race.
 */
public interface RaceTracker extends Listener {

  /**
   * Determines if a given player is being tracked by this tracker.
   *
   * @param playerId The ID of the player.
   * @return Whether or not the player is being tracked.
   */
  boolean isInRace(UUID playerId);


  /**
   * Gets the current runtime of the player's run if the player is not being tracked this will throw
   * an IllegalArgumentException.
   *
   * @throws IllegalArgumentException
   * @param playerId The ID of the player.
   * @return The time of the player.
   */
  long getCurrentTime(UUID playerId) throws IllegalArgumentException;

}
