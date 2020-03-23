package com.kyleposluns.elytrarace.map;


import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.event.Listener;

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
   * Return an object responsible for tracking players in this arena.
   * @return This arena's RaceTracker.
   */
  RaceTracker getRaceTracker();

  /**
   * Return an object responsible for keeping records about the fasting times in this arena.
   * @return This arena's recordbook.
   */
  RecordBook getRecordBook();

}
