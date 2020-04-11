package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.DrawOutline;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class RaceTrackerImpl implements RaceTracker {

  private final Set<UUID> tracking;

  private final Map<UUID, Long> startTimes;

  private final List<Area> areas;

  private final Map<UUID, Integer> playerCheckpoints;

  private final Map<UUID, Integer> locationTrackingThreads;

  private final Map<UUID, Integer> particleThreads;

  private final Map<UUID, List<Vector>> trackedLocations;

  private List<Vector> fastestPath;

  private final Location spawn;

  private final RecordBook recordBook;

  private final ElytraDatabase database;

  private final UUID arenaId;

  private final Plugin plugin;



  public RaceTrackerImpl(Plugin plugin, ElytraDatabase database, UUID arenaId, List<Area> areas,
      RecordBook recordBook,
      Location spawn) {
    this.plugin = plugin;
    this.database = database;
    this.arenaId = arenaId;
    this.tracking = new HashSet<>();
    this.startTimes = new HashMap<>();
    this.locationTrackingThreads = new HashMap<>();
    this.particleThreads = new HashMap<>();
    this.areas = areas;
    this.trackedLocations = new HashMap<>();
    this.playerCheckpoints = new HashMap<>();
    this.spawn = spawn;
    this.recordBook = recordBook;
    Record top = this.recordBook.getTopRecord();
    if (top == null) {
      this.fastestPath = new ArrayList<>();
    } else {
      this.fastestPath = this.recordBook.getTopRecord().getPositions();
    }
  }

  private void nextCheckpoint(UUID playerId) {
    int next = this.playerCheckpoints.get(playerId) + 1;
    this.playerCheckpoints.put(playerId, next);
  }

  @Override
  public void startRace(UUID playerId) {
    this.startTimes.put(playerId, System.currentTimeMillis());
    this.playerCheckpoints.put(playerId, 1);
    this.trackedLocations.put(playerId, new ArrayList<>());
    this.locationTrackingThreads
        .put(playerId, plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
          Player player = this.plugin.getServer().getPlayer(playerId);
          if (player == null) {
            return;
          }
          Vector position = player.getLocation().toVector();
          this.trackedLocations.get(playerId).add(position);
        }, 0L, 10L));
    this.particleThreads
        .put(playerId, plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
          Player player = Bukkit.getPlayer(playerId);
          if (player == null) {
            return;
          }
          DustOptions ringDustOptions = new DustOptions(Color.fromRGB(255, 0, 0), 1);
          for (Area area : this.areas) {
            for (Vector v : area.visitArea(new DrawOutline())) {
              player.spawnParticle(Particle.REDSTONE,
                  new Location(player.getWorld(), v.getX(), v.getY(), v.getZ()), 4, ringDustOptions);
            }
          }
          DustOptions fastestDustOptions = new DustOptions(Color.fromRGB(255, 0, 0), 1);
          for (Vector v : this.fastestPath) {
            player.spawnParticle(Particle.REDSTONE,
                new Location(player.getWorld(), v.getX(), v.getY(), v.getZ()), 4, fastestDustOptions);
          }

        }, 0L, 5L));
  }

  @Override
  public void endRace(UUID playerId) {
    this.startTimes.remove(playerId);
    this.playerCheckpoints.remove(playerId);
    this.trackedLocations.remove(playerId);
    this.plugin.getServer().getScheduler().cancelTask(this.locationTrackingThreads.get(playerId));
    this.locationTrackingThreads.remove(playerId);
    this.database.saveRecordBook(this.recordBook);
    Record top = this.recordBook.getTopRecord();
    if (top == null) {
      this.fastestPath = new ArrayList<>();
    } else {
      this.fastestPath = this.recordBook.getTopRecord().getPositions();
    }
  }

  @Override
  public boolean isRacing(UUID playerId) {
    return this.startTimes.containsKey(playerId);
  }

  @Override
  public void track(UUID playerId) {
    this.tracking.add(playerId);
  }

  @Override
  public void unTrack(UUID playerId) {
    if (this.isRacing(playerId)) {
      this.endRace(playerId);
    }
    this.tracking.remove(playerId);
  }

  @Override
  public boolean isTracking(UUID playerId) {
    return this.tracking.contains(playerId);
  }

  @Override
  public long getCurrentTime(UUID playerId) throws IllegalArgumentException {
    return Optional.of(this.startTimes.get(playerId))
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("The player %s is not currently racing.", playerId.toString())));
  }

  @EventHandler
  public void onEnterCheckpoint(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    UUID playerId = event.getPlayer().getUniqueId();

    Area area = this.areas.get(this.playerCheckpoints.getOrDefault(playerId, 0));
    if (!this.isTracking(playerId) || (area.contains(event.getFrom().toVector()) && !area
        .contains(event.getTo().toVector()))) {
      return;
    }

    if (!this.isRacing(playerId)) {
      this.startRace(playerId);
      event.getPlayer().sendMessage(ChatColor.GREEN + "Good Luck!");
    } else {
      if (this.playerCheckpoints.get(playerId) == this.playerCheckpoints.size() - 1) {
        long date = System.currentTimeMillis();
        int time = (int) (date - this.startTimes.get(playerId));
        Record record = new RecordBuilder()
            .arenaId(this.arenaId)
            .playerId(playerId)
            .time(time)
            .date(date)
            .positions(this.trackedLocations.getOrDefault(playerId, new ArrayList<>()))
            .build();
        this.recordBook.report(record);
        this.endRace(playerId);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Finished!");

      } else {
        this.nextCheckpoint(playerId);
        event.getPlayer().sendMessage(ChatColor.GREEN + "Checkpoint!");
      }
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    Player player = event.getPlayer();
    Area start = this.areas.get(0);


    if (!(this.isRacing(player.getUniqueId())) || start.contains(player.getLocation().toVector())) {
      return;
    }

    if (player.getLocation().subtract(0, 2, 0).getBlock().getRelative(BlockFace.DOWN).getType()
        != Material.AIR) {
      player.teleport(this.spawn);
      this.endRace(player.getUniqueId());
    }
  }

}
