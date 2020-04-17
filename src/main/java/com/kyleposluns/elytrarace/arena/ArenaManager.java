package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.List;

/**
 * Manages Arenas in the game.
 */
public interface ArenaManager {

  /**
   * Checks if this arena manager has an arena by the name arenaName
   * @param arenaName The name of the arena.
   * @return True if this arena manager has an arena by the given name.
   */
  boolean hasArena(String arenaName);

  /**
   * Find an arena by name
   * @param arenaName The name of the arena.
   * @return The arena or null if it doesn't exit.
   */
  Arena getArena(String arenaName);


  RaceTracker getRaceTracker(String arena);

  /**
   * Find the list of all loaded arenas.
   * @return The arena names.
   */
  List<String> getLoadedArenas();

  List<String> getArenaDisplayNames();


}
