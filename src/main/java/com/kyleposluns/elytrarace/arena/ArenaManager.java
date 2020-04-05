package com.kyleposluns.elytrarace.arena;

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
   * @return The arena.
   * @throws IllegalArgumentException If the arena doesn't exist.
   */
  Arena getArena(String arenaName) throws IllegalArgumentException;

  /**
   * Find the list of all loaded arenas.
   * @return The arena names.
   */
  List<String> getLoadedArenas();


}
