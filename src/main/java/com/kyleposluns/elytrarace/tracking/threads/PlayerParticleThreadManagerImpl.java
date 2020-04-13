package com.kyleposluns.elytrarace.tracking.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class PlayerParticleThreadManagerImpl implements PlayerParticleThreadManager {

  private long delay;

  private long duration;

  private int count;

  private Map<UUID, List<Integer>> playerThreads;

  private Map<UUID, List<ParticleDisplay>> particleHandlers;

  private Plugin plugin;

  private BukkitScheduler scheduler;

  public PlayerParticleThreadManagerImpl(Plugin plugin, long delay, long duration, int count) {
    this.plugin = plugin;
    this.delay = delay;
    this.duration = duration;
    this.count = count;
    this.scheduler = this.plugin.getServer().getScheduler();
    this.playerThreads = new ConcurrentHashMap<>();
    this.particleHandlers = new ConcurrentHashMap<>();
  }

  @Override
  public void showParticles(UUID playerId, Color color, List<Vector> locations) {
    if (!this.playerThreads.containsKey(playerId)) {
      this.playerThreads.put(playerId, new ArrayList<>());
    }

    ParticleDisplay display = new ParticleDisplay(playerId, color, locations, this.count);
    if (!this.particleHandlers.containsKey(playerId)) {
      this.particleHandlers.put(playerId, new ArrayList<>());
    }
    this.particleHandlers.get(playerId).add(display);


    this.playerThreads.get(playerId)
        .add(this.scheduler.scheduleSyncRepeatingTask(this.plugin, display
            , this.delay, this.duration));
  }

  @Override
  public void flashColor(UUID playerId, Color color, long delay) {
    for (ParticleDisplay display : this.particleHandlers.getOrDefault(playerId, new ArrayList<>())) {
      Color oldColor = display.getOldColor();
      display.setColor(color);
      this.scheduler.scheduleSyncDelayedTask(this.plugin, () -> display.setColor(oldColor), delay);
    }


  }

  @Override
  public void stopShowingParticles(UUID playerId) {
    this.playerThreads.getOrDefault(playerId, new ArrayList<>())
        .forEach(id -> scheduler.cancelTask(id));
    this.particleHandlers.remove(playerId);
    this.playerThreads.remove(playerId);
  }
}
