package com.kyleposluns.elytrarace.tracking;

import java.util.UUID;
import org.bukkit.util.Vector;

public interface PlayerCheckpointTracker {

  boolean isInNextCheckpoint(UUID playerId, Vector position);

  void nextCheckpoint(UUID playerId);

  boolean passedAllCheckpoints(UUID playerId);

  boolean isFlying(UUID player);

  void addPlayer(UUID playerId);

  long getStartTime(UUID playerId);

  void removePlayer(UUID playerId);

}
