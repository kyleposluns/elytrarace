package com.kyleposluns.elytrarace.tracking.threads;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface PlayerParticleThreadManager {

  void showParticles(UUID playerId, Color color, Function<Player, List<Vector>> locations);

  void flashColor(UUID playerId, Color color, long delay);

  void stopShowingParticles(UUID playerId);

}
