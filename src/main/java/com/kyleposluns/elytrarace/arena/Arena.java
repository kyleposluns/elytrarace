package com.kyleposluns.elytrarace.arena;


import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Represents an arena in a world.
 */
public interface Arena extends Listener {

  /**
   * The unique ID of this arena.
   * @return This arena's id.
   */
  UUID getArenaId();

  /**
   * The unique ID of the world in which this arena exists.
   * @return The id of this arena's world.
   */
  UUID getWorldId();

  /**
   * The location that players should spawn in this Arena.
   * The location's world must match the world described in getWorldId()
   *
   * @return The spawn point of this arena.
   */
  Location getSpawn();

  /**
   * The name of this arena.
   */
  String getName();

  /**
   * Return an object responsible for tracking players in this arena.
   * @param plugin The plugin that is responsible for game threads.
   * @param database The database responsible for saving/loading data.
   * @return This arena's RaceTracker.
   */
  RaceTracker createRaceTracker(Plugin plugin, ElytraDatabase database);

  /**
   * Return an object responsible for keeping records about the fasting times in this arena.
   * @return This arena's recordbook.
   */
  RecordBook getRecordBook();

}
