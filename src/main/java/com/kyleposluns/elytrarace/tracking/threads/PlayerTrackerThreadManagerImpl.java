package com.kyleposluns.elytrarace.tracking.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class PlayerTrackerThreadManagerImpl implements PlayerTrackerThreadManager {

  private Map<UUID, Integer> playerThreads;

  private Map<UUID, List<Vector>> playerLocations;

  private BukkitScheduler scheduler;

  private Plugin plugin;

  private long delay;

  private long duration;

  public PlayerTrackerThreadManagerImpl(Plugin plugin, long delay, long duration) {
    this.plugin = plugin;
    this.scheduler = plugin.getServer().getScheduler();
    this.delay = delay;
    this.duration = duration;
    this.playerThreads = new ConcurrentHashMap<>();
    this.playerLocations = new ConcurrentHashMap<>();
  }

  @Override
  public void trackLocations(UUID playerId) {
    if (!playerLocations.containsKey(playerId)) {
      this.playerLocations.put(playerId, new ArrayList<>());
    }

    if (this.playerThreads.containsKey(playerId)) {
      this.stopTracking(playerId);
    }

    this.playerThreads.put(playerId, this.scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      Player player = this.plugin.getServer().getPlayer(playerId);
      if (player == null) {
        return;
      }
      this.playerLocations.get(playerId).add(player.getEyeLocation().toVector());
    }, this.delay, this.duration));
  }

  @Override
  public void stopTracking(UUID playerId) {
    if (!playerThreads.containsKey(playerId)) {
      throw new IllegalStateException(
          String.format("The player %s is not being tracked.", playerId.toString()));
    }
    this.scheduler.cancelTask(this.playerThreads.get(playerId));
    this.playerThreads.remove(playerId);
  }

  @Override
  public List<Vector> getTrackedLocations(UUID playerId) {
    if (!playerThreads.containsKey(playerId)) {
      throw new IllegalStateException(
          String.format("The player %s is not being tracked.", playerId.toString()));
    }
    return this.playerLocations.get(playerId);
  }
}
