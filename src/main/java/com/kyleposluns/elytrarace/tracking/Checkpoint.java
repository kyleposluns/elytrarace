package com.kyleposluns.elytrarace.tracking;

import java.util.UUID;
import org.bukkit.event.Listener;

/**
 * Represents a checkpoint in an arena.
 */
public interface Checkpoint extends Listener {


  /**
   * Determines if a player has made it through the checkpoint.
   * @param player The player.
   * @return Whether or not the player has passed.
   */
  boolean passed(UUID player);

}
