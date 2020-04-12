package com.kyleposluns.elytrarace.tracking.threads;

import java.util.List;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.util.Vector;

public interface PlayerParticleThreadManager {

  void showParticles(UUID playerId, Color color, List<Vector> locations);

  void stopShowingParticles(UUID playerId);

}
