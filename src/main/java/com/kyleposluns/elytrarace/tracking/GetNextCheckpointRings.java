package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.DrawOutline;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class GetNextCheckpointRings implements CheckpointTrackerVisitor<List<Vector>> {

  private final UUID player;

  private final int n;

  public GetNextCheckpointRings(UUID player, int n) {
    this.player = player;
    this.n = n;
  }

  @Override
  public List<Vector> visitCheckpointTrackerImpl(CheckpointTrackerImpl checkpointTracker) {
    int max = checkpointTracker.getCheckpoints().size();

    int currentCheckpoint = checkpointTracker.getCurrentCheckpointMap().get(this.player);
    int i = currentCheckpoint;
    List<Vector> points = new ArrayList<>();
    while (i < currentCheckpoint + this.n && i < max) {
      Area area = checkpointTracker.getCheckpoints().get(i);
      points.addAll(area.visitArea(new DrawOutline()));
      i = i + 1;
    }

    return points;

  }
}
