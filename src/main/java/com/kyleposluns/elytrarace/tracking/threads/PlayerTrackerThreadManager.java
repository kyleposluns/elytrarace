package com.kyleposluns.elytrarace.tracking.threads;

import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public interface PlayerTrackerThreadManager {

  void trackLocations(UUID playerId);

  void stopTracking(UUID playerId);

  List<Vector> getTrackedLocations(UUID playerId);

}
