package com.kyleposluns.elytrarace.tracking;

import java.util.List;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GetDisplayedParticleLocations implements Function<Player, List<Vector>> {

  private final CheckpointTracker tracker;

  private final int n;

  public GetDisplayedParticleLocations(CheckpointTracker tracker, int n) {
    this.tracker = tracker;
    this.n = n;
  }

  @Override
  public List<Vector> apply(Player player) {
    return tracker.visitCheckpointTracker(new GetNextCheckpointRings(player.getUniqueId(), this.n));
  }
}
