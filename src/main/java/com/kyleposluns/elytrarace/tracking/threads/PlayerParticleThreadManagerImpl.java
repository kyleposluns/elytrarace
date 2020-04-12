package com.kyleposluns.elytrarace.tracking.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class PlayerParticleThreadManagerImpl implements PlayerParticleThreadManager {

  private long delay;

  private long duration;

  private int count;

  private Map<UUID, List<Integer>> playerThreads;

  private Plugin plugin;

  private BukkitScheduler scheduler;

  public PlayerParticleThreadManagerImpl(Plugin plugin, long delay, long duration, int count) {
    this.plugin = plugin;
    this.delay = delay;
    this.duration = duration;
    this.count = count;
    this.scheduler = this.plugin.getServer().getScheduler();
    this.playerThreads = new ConcurrentHashMap<>();
  }

  @Override
  public void showParticles(UUID playerId, Color color, List<Vector> locations) {
    if (!this.playerThreads.containsKey(playerId)) {
      this.playerThreads.put(playerId, new ArrayList<>());
    }

    this.playerThreads.get(playerId).add(this.scheduler.scheduleSyncRepeatingTask(this.plugin,
        () -> {
          Player player = Bukkit.getPlayer(playerId);
          if (player == null) {
            return;
          }
          DustOptions ringDustOptions = new DustOptions(color, 1);
          for (Vector v : locations) {
            player.spawnParticle(Particle.REDSTONE,
                new Location(player.getWorld(), v.getX(), v.getY(), v.getZ()), this.count,
                ringDustOptions);
          }
        }, this.delay, this.duration));
  }

  @Override
  public void stopShowingParticles(UUID playerId) {
    this.playerThreads.getOrDefault(playerId, new ArrayList<>()).forEach(id -> scheduler.cancelTask(id));
    this.playerThreads.remove(playerId);
  }
}
