package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.AreaType;
import com.kyleposluns.elytrarace.arena.area.DrawOutline;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBuilder;
import com.kyleposluns.elytrarace.tracking.threads.PlayerParticleThreadManager;
import com.kyleposluns.elytrarace.tracking.threads.PlayerParticleThreadManagerImpl;
import com.kyleposluns.elytrarace.tracking.threads.PlayerTrackerThreadManager;
import com.kyleposluns.elytrarace.tracking.threads.PlayerTrackerThreadManagerImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class RaceTrackerImpl implements RaceTracker {

  private final Location spawn;

  private final RecordBook recordBook;

  private final ElytraDatabase database;

  private final UUID arenaId;

  private final PlayerTrackerThreadManager playerTrackerThreadManager;

  private final PlayerParticleThreadManager playerParticleThreadManager;

  private final PlayerCheckpointTracker playerCheckpointTracker;

  private final List<Vector> checkpointParticles;

  private final Area start;

  private final Area goal;

  private final Set<UUID> players;

  private List<Vector> fastestPath;

  public RaceTrackerImpl(Plugin plugin, ElytraDatabase database, UUID arenaId, List<Area> areas,
      RecordBook recordBook,
      Location spawn) {
    this.spawn = spawn;
    this.recordBook = recordBook;
    this.arenaId = arenaId;
    this.database = database;
    this.checkpointParticles = areas.stream()
        .filter(area -> area.getType() == AreaType.CHECKPOINT)
        .flatMap(area -> area.visitArea(new DrawOutline()).stream()).collect(
            Collectors.toList());
    this.players = new HashSet<>();
    this.playerTrackerThreadManager = new PlayerTrackerThreadManagerImpl(plugin, 0L, 5L);
    this.playerParticleThreadManager = new PlayerParticleThreadManagerImpl(plugin, 0L, 5L, 4);
    this.playerCheckpointTracker = new PlayerCheckpointTrackerImpl(
        areas.stream().filter(area -> area.getType() == AreaType.CHECKPOINT).collect(
            Collectors.toList()));
    this.start = areas.stream().filter(area -> area.getType() == AreaType.START).findFirst()
        .orElseThrow();
    this.goal = areas.stream().filter(area -> area.getType() == AreaType.END).findFirst()
        .orElseThrow();
    this.fastestPath = this.updateFastestPath();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }


  private List<Vector> updateFastestPath() {
    Record record = this.recordBook.getTopRecord();
    if (record == null) {
      return new ArrayList<>();
    } else {
      return record.getPositions();
    }
  }


  @Override
  public boolean isRacing(UUID playerId) {
    return this.playerCheckpointTracker.isFlying(playerId);
  }

  @Override
  public void track(UUID playerId) {
    this.players.add(playerId);
  }

  @Override
  public void unTrack(UUID playerId) {
    if (this.isRacing(playerId)) {
      this.endRace(playerId);
    }
    this.players.remove(playerId);
  }

  @Override
  public boolean isTracking(UUID playerId) {
    return players.contains(playerId);
  }

  @Override
  public void startRace(UUID playerId) {
    this.playerCheckpointTracker.addPlayer(playerId);
    this.playerParticleThreadManager.showParticles(playerId, Color.RED, this.checkpointParticles);
    if (!this.fastestPath.isEmpty()) {
      this.playerParticleThreadManager.showParticles(playerId, Color.YELLOW, this.fastestPath);
    }
    this.playerTrackerThreadManager.trackLocations(playerId);
  }

  private void finishRace(UUID playerId) {
    long end = System.currentTimeMillis();
    long start = this.playerCheckpointTracker.getStartTime(playerId);

    List<Vector> positions = this.playerTrackerThreadManager.stopTracking(playerId);
    Record record = new RecordBuilder()
        .date(end)
        .time((int) (end - start))
        .playerId(playerId)
        .arenaId(this.arenaId)
        .positions(positions)
        .build();
    this.recordBook.report(record);
    this.database.saveRecordBook(this.recordBook);
    this.endHelp(playerId);
  }


  @Override
  public void endRace(UUID playerId) {
    this.playerTrackerThreadManager.stopTracking(playerId);
    this.endHelp(playerId);
  }

  private void endHelp(UUID playerId) {
    this.playerParticleThreadManager.stopShowingParticles(playerId);
    this.playerCheckpointTracker.removePlayer(playerId);
    this.fastestPath = this.updateFastestPath();
  }

  @EventHandler
  public void onLeaveStart(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    UUID playerId = event.getPlayer().getUniqueId();

    if (!this.isTracking(playerId)) {
      return;
    }

    if (this.start.contains(event.getFrom().toVector()) && !this.start
        .contains(event.getTo().toVector()) && !this.isRacing(playerId)) {
      this.startRace(playerId);
      event.getPlayer().sendMessage(ChatColor.GREEN + "Good Luck!");

    }

  }


  @EventHandler
  public void onEnterGoal(PlayerMoveEvent event) {

    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    UUID playerId = event.getPlayer().getUniqueId();

    if (!this.isRacing(playerId)) {
      return;
    }

    if (!this.goal.contains(event.getFrom().toVector()) && this.goal
        .contains(event.getTo().toVector()) && this.playerCheckpointTracker
        .passedAllCheckpoints(playerId)) {
      this.finishRace(playerId);
      event.getPlayer().sendMessage(ChatColor.GREEN + "Congratulations!");
      event.getPlayer().teleport(this.spawn);

    }

  }

  @EventHandler
  public void onEnterCheckpoint(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    UUID playerId = event.getPlayer().getUniqueId();

    if (!(this.isRacing(playerId)) || this.start
        .contains(event.getPlayer().getLocation().toVector())
        || this.goal.contains(event.getPlayer().getLocation().toVector())) {
      return;
    }

    if (this.playerCheckpointTracker
        .isInNextCheckpoint(playerId, event.getPlayer().getEyeLocation().toVector())) {
      this.playerCheckpointTracker.nextCheckpoint(playerId);
      event.getPlayer().sendMessage(ChatColor.GREEN + "Checkpoint!");
    }


  }

  @EventHandler
  public void onFall(PlayerMoveEvent event) {
    if (event.getTo() == null || event.getFrom().toVector().equals(event.getTo().toVector())) {
      return;
    }

    Player player = event.getPlayer();

    if (!this.isRacing(player.getUniqueId()) || this.start.contains(event.getTo().toVector())
        || this.goal.contains(event.getTo().toVector())) {
      return;
    }

    if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()
        != Material.AIR) {
      this.endRace(player.getUniqueId());
      player.teleport(this.spawn);
    }
  }

}
