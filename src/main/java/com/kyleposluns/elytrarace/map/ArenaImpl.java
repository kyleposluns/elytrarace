package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.ElytraRace;
import com.kyleposluns.elytrarace.records.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class ArenaImpl implements Arena {

  private static final String PLAYER_DATA = "playerdata";

  private Location spawn;

  private RecordBook records;

  private ArenaInfo arenaInfo;

  private Set<UUID> activePlayers;

  private Map<UUID, ActiveRun> activeRuns;

  public ArenaImpl(ArenaInfo arenaInfo, File arenaFile) {
    this.arenaInfo = arenaInfo;
    File playerDataFile = new File(arenaFile, PLAYER_DATA);
    playerDataFile.mkdirs();
    this.records =
            new RecordBookImpl(playerDataFile);
    this.activePlayers = new HashSet<>();
    this.activeRuns = new HashMap<>();
    World spawnWorld = Bukkit.getWorld(this.arenaInfo.getWorldId());
    Vector spawnVector = this.arenaInfo.getSpawn();
    float spawnYaw = this.arenaInfo.getSpawnYaw();
    float spawnPitch = this.arenaInfo.getSpawnPitch();
    this.spawn = new Location(spawnWorld, spawnVector.getX(), spawnVector.getY(),
            spawnVector.getZ(), spawnYaw, spawnPitch);
  }

  @Override
  public void activate(Player player) {
    this.activePlayers.add(player.getUniqueId());
    this.reset(player);
  }

  @Override
  public void reset(Player player) {
    this.activeRuns.remove(player.getUniqueId());
    player.teleport(this.spawn);
  }

  @Override
  public void deactivate(Player player) {
    this.activeRuns.remove(player.getUniqueId());
    this.activePlayers.remove(player.getUniqueId());
  }

  @Override
  public void startRun(Player player) {
    ActiveRun run = new ActiveRun(player);
    this.activeRuns.put(player.getUniqueId(), run);
    run.runTaskTimer(ElytraRace.getInstance(), 0L, 20L);
  }

  @Override
  public void finishRun(Player player, RaceResult result) {
    ActiveRun run = this.activeRuns.get(player.getUniqueId());
    if (run != null) {
      Record record = run.finish(result);
      this.records.addRecord(record);
    }
    this.reset(player);
  }

  @Override
  public double getStatistic(Function<RecordBook, Double> function) {
    return function.apply(this.records);
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    if (!this.isActivated(player)) return;
    if (event.getTo() == null) return;

    Vector to = event.getTo().toVector();
    Vector from = event.getFrom().toVector();

    if (Area.isExiting(this.arenaInfo.getStart(), to, from) && !isOnRun(player)) {
      this.startRun(player);
    } else if (Area.isEntering(this.arenaInfo.getGoal(), to, from) && isOnRun(player)) {
      this.finishRun(player, RaceResult.SUCCESS);
    } else if (this.arenaInfo.getBorders().stream().anyMatch(area -> Area.isEntering(area, to,
            from))) {
      this.finishRun(player, RaceResult.FAILED);
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (isOnRun(player)) {
      this.finishRun(player, RaceResult.DISQUALIFIED);
    }
  }

  private boolean isActivated(Player player) {
    return this.activePlayers.contains(player.getUniqueId());
  }

  private boolean isOnRun(Player player) {
    return this.isActivated(player) && this.activeRuns.containsKey(player.getUniqueId());
  }

}
