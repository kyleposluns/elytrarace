package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.area.Area;
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
import org.bukkit.Location;
import org.bukkit.Material;
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

  private final Map<UUID, List<Vector>> trackedLocations;

  private final Location spawn;

  private final RecordBook recordBook;

  private final ElytraDatabase database;

  private final UUID arenaId;

  private final Plugin plugin;

  public RaceTrackerImpl(Plugin plugin, ElytraDatabase database, UUID arenaId, List<Area> areas, RecordBook recordBook,
      Location spawn) {
    this.plugin = plugin;
    this.database = database;
    this.arenaId = arenaId;
    this.tracking = new HashSet<>();
    this.startTimes = new HashMap<>();
    this.locationTrackingThreads = new HashMap<>();
    this.areas = areas;
    this.trackedLocations = new HashMap<>();
    this.playerCheckpoints = new HashMap<>();
    this.spawn = spawn;
    this.recordBook = recordBook;
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
  }

  @Override
  public void endRace(UUID playerId) {
    this.startTimes.remove(playerId);
    this.playerCheckpoints.remove(playerId);
    this.trackedLocations.remove(playerId);
    this.plugin.getServer().getScheduler().cancelTask(this.locationTrackingThreads.get(playerId));
    this.locationTrackingThreads.remove(playerId);
    this.database.saveRecordBook(this.recordBook);
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
    if (!this.isTracking(playerId) || area.contains(event.getFrom().toVector()) || !area
        .contains(event.getTo().toVector())) {
      return;
    }

    if (!this.isRacing(playerId)) {
      this.startRace(playerId);
    } else {
      if (this.playerCheckpoints.get(playerId) == this.areas.size() - 1) {
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
      } else {
        this.nextCheckpoint(playerId);
      }
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }
    Player player = event.getPlayer();

    if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()
        != Material.AIR) {
      player.teleport(this.spawn);
      this.endRace(player.getUniqueId());
    }
  }

}
