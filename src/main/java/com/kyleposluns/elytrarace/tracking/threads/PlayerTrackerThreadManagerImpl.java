package com.kyleposluns.elytrarace.tracking.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    this.playerThreads = new HashMap<>();
    this.playerLocations = new HashMap<>();
  }

  @Override
  public void trackLocations(UUID playerId) {
    if (playerThreads.containsKey(playerId)) {
      stopTracking(playerId);
    }

    this.playerLocations.put(playerId, new ArrayList<>());
    int id = this.scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      Player player = this.plugin.getServer().getPlayer(playerId);
      if (player == null) {
        return;
      }
      this.playerLocations.get(playerId).add(player.getEyeLocation().toVector());
    }, this.delay, this.duration);
    this.playerThreads.put(playerId, id);
  }

  @Override
  public List<Vector> stopTracking(UUID playerId) {
    if (!playerThreads.containsKey(playerId)) {
      return new ArrayList<>();
    }
    int id = this.playerThreads.remove(playerId);

    this.scheduler.cancelTask(id);
    List<Vector> positions = this.playerLocations.remove(playerId);

    return positions != null && !positions.isEmpty() ? positions : new ArrayList<>();
  }
}
