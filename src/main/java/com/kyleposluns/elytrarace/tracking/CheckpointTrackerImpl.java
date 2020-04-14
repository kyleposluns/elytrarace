package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.area.Area;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.util.Vector;

public class CheckpointTrackerImpl implements CheckpointTracker {

  private final List<Area> checkpoints;

  private final Map<UUID, Long> startTime;

  private final Map<UUID, Integer> currentCheckpoints;

  public CheckpointTrackerImpl(List<Area> areas) {
    this.checkpoints = areas;
    this.currentCheckpoints = new ConcurrentHashMap<>();
    this.startTime = new ConcurrentHashMap<>();
  }

  List<Area> getCheckpoints() {
    return List.copyOf(checkpoints);
  }

  Map<UUID, Long> getStartTimeMap() {
    return Map.copyOf(this.startTime);
  }

  Map<UUID, Integer> getCurrentCheckpointMap() {
    return Map.copyOf(this.currentCheckpoints);
  }

  @Override
  public boolean isInNextCheckpoint(UUID playerId, Vector position) {
    return this.checkpoints.get(this.currentCheckpoints.getOrDefault(playerId, 0))
        .contains(position);
  }

  @Override
  public void nextCheckpoint(UUID playerId) {
    int i = this.currentCheckpoints.getOrDefault(playerId, 0);
    this.currentCheckpoints.put(playerId, i + 1);
  }

  @Override
  public boolean passedAllCheckpoints(UUID playerId) {
    return this.currentCheckpoints.getOrDefault(playerId, 0) == this.checkpoints.size();
  }

  @Override
  public boolean isFlying(UUID player) {
    return this.currentCheckpoints.containsKey(player);
  }

  @Override
  public void addPlayer(UUID playerId) {
    this.startTime.put(playerId, System.currentTimeMillis());
    this.currentCheckpoints.put(playerId, 0);
  }

  @Override
  public long getStartTime(UUID playerId) {
    if (!this.startTime.containsKey(playerId)) {
      throw new IllegalArgumentException(
          String.format("Do not have %s start time on file.", playerId.toString()));
    }

    return this.startTime.get(playerId);
  }

  @Override
  public void removePlayer(UUID playerId) {
    this.currentCheckpoints.remove(playerId);
  }

  @Override
  public <R> R visitCheckpointTracker(CheckpointTrackerVisitor<R> visitor) {
    return visitor.visitCheckpointTrackerImpl(this);
  }
}
