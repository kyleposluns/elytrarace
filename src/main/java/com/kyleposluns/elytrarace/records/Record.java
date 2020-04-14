package com.kyleposluns.elytrarace.records;

import java.util.UUID;

/**
 * An Record holds a player, the time they took to complete a course and all of the positions that
 * they took to get to the goal.
 */
public interface Record extends Comparable<Record> {

  /**
   * Gets the ID of the player tied to the record.
   * @return The ID of the player.
   */
  UUID getPlayerId();

  /**
   * Gets the ID of the arena tied to the record.
   * @return The ID of the record.
   */
  UUID getArenaId();

  /**
   * Gets the date the record occurred in milliseconds.
   */
  long getDate();

  /**
   * Gets the amount of time the player took to complete the course.
   * @return The time in seconds.
   */
  int getTime();

}
