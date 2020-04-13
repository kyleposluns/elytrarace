package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.area.Area;
import com.kyleposluns.elytrarace.arena.area.AreaType;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBuilder;
import com.kyleposluns.elytrarace.tracking.threads.PlayerParticleThreadManager;
import com.kyleposluns.elytrarace.tracking.threads.PlayerParticleThreadManagerImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class RaceTrackerImpl implements RaceTracker {

  private final Location spawn;

  private final RecordBook recordBook;

  private final ElytraDatabase database;

  private final UUID arenaId;

  private final PlayerParticleThreadManager playerParticleThreadManager;

  private final CheckpointTracker checkpointTracker;

  private final Area start;

  private final Area goal;

  private final Set<UUID> players;

  private final List<Vector> path;

  private final MessageFormatter messageFormatter;

  private final ItemStack elytra;

  public RaceTrackerImpl(Plugin plugin, MessageFormatter messageFormatter, ElytraDatabase database,
      UUID arenaId, List<Area> areas, List<Vector> path,
      RecordBook recordBook,
      Location spawn) {
    this.spawn = spawn;
    this.messageFormatter = messageFormatter;
    this.recordBook = recordBook;
    this.arenaId = arenaId;
    this.database = database;
    this.path = path;
   /* this.checkpointParticles = areas.stream()
        .filter(area -> area.getType() == AreaType.CHECKPOINT)
        .flatMap(area -> area.visitArea(new DrawOutline()).stream()).collect(
            Collectors.toList());*/
    this.players = new HashSet<>();
    this.playerParticleThreadManager = new PlayerParticleThreadManagerImpl(plugin, 0L, 5L, 1);
    this.checkpointTracker = new CheckpointTrackerImpl(
        areas.stream().filter(area -> area.getType() == AreaType.CHECKPOINT).collect(
            Collectors.toList()));
    this.start = areas.stream().filter(area -> area.getType() == AreaType.START).findFirst()
        .orElseThrow();
    this.goal = areas.stream().filter(area -> area.getType() == AreaType.END).findFirst()
        .orElseThrow();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);

    this.elytra = new ItemStack(Material.ELYTRA, 1);
    ItemMeta meta = this.elytra.getItemMeta();
    if (meta != null) {
      meta.setUnbreakable(true);
      this.elytra.setItemMeta(meta);
    }

  }


  @Override
  public boolean isRacing(UUID playerId) {
    return this.checkpointTracker.isFlying(playerId);
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
    this.checkpointTracker.addPlayer(playerId);
    this.playerParticleThreadManager.showParticles(playerId, Color.RED,
        new GetDisplayedParticleLocations(this.checkpointTracker, 2));

    if (!this.path.isEmpty()) {
      this.playerParticleThreadManager
          .showParticles(playerId, Color.YELLOW, (player) -> this.path);
    }
  }

  private long finishRace(UUID playerId) {
    long end = System.currentTimeMillis();
    long start = this.checkpointTracker.getStartTime(playerId);
    Record record = new RecordBuilder()
        .date(end)
        .time((int) (end - start))
        .playerId(playerId)
        .arenaId(this.arenaId)
        .build();
    this.recordBook.report(record);
    this.database.saveRecordBook(this.recordBook);
    this.endHelp(playerId);
    return end - start;
  }


  @Override
  public void endRace(UUID playerId) {
    this.endHelp(playerId);
  }

  private void endHelp(UUID playerId) {
    this.playerParticleThreadManager.stopShowingParticles(playerId);
    this.checkpointTracker.removePlayer(playerId);
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
      event.getPlayer().getInventory().setChestplate(this.elytra);
      this.startRace(playerId);
      this.messageFormatter.sendPrefixedMessage(event.getPlayer(), ChatColor.GREEN + "Good Luck!");
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
        .contains(event.getTo().toVector()) && this.checkpointTracker
        .passedAllCheckpoints(playerId)) {
      long time = this.finishRace(playerId);
      this.messageFormatter
          .sendPrefixedMessage(event.getPlayer(),
              ChatColor.GREEN + "Congratulations!\n" + ChatColor.YELLOW + "Time: "
                  + this.messageFormatter
                  .formatTime(time));
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

    if (!this.checkpointTracker.passedAllCheckpoints(playerId) && this.checkpointTracker
        .isInNextCheckpoint(playerId, event.getPlayer().getEyeLocation().toVector())) {
      this.checkpointTracker.nextCheckpoint(playerId);
      event.getPlayer()
          .playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
      this.playerParticleThreadManager.flashColor(playerId, Color.fromRGB(0x23CB23), 10L);

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

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    if (!event.getWorld().equals(this.spawn.getWorld())) {
      return;
    }
    boolean rain = event.toWeatherState();
    if (rain) {
      event.setCancelled(true);
    }


  }
}
