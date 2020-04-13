package com.kyleposluns.elytrarace.tracking.threads;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleDisplay implements Runnable {

  private final UUID playerId;

  private Color color;

  private final int count;

  private final Function<Player, List<Vector>> locations;

  public ParticleDisplay(UUID playerId, Color color, int count, Function<Player, List<Vector>> locations) {
    this.playerId = playerId;
    this.color = color;
    this.locations = locations;
    this.count = count;
  }

  public Color getOldColor() {
    return this.color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public void run() {
    Player player = Bukkit.getPlayer(playerId);
    if (player == null) {
      return;
    }
    DustOptions ringDustOptions = new DustOptions(color, 1);
    for (Vector v : locations.apply(player)) {
      player.spawnParticle(Particle.REDSTONE,
          new Location(player.getWorld(), v.getX(), v.getY(), v.getZ()), this.count,
          ringDustOptions);
    }
  }
}
