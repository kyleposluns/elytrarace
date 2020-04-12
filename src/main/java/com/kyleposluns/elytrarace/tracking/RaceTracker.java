package com.kyleposluns.elytrarace.tracking;


import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
   * Begin tracking the player.
   */
  void track(UUID playerId);

  /**
   * Stop tracking the player with this tracker.
   */
  void unTrack(UUID playerId);

  /**
   * Determines if a player is being tracked by this tracker. This does not imply that
   * the player is currently racing.
   */
  boolean isTracking(UUID playerId);

  /**
   * Start the race for a player.
   * @param playerId The ID of the player.
   */
  void startRace(UUID playerId);

  /**
   * End the race for a player.
   * @param playerId The ID of the player.
   */
  void endRace(UUID playerId);


}
