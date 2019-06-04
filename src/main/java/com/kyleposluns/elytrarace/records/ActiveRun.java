package com.kyleposluns.elytrarace.records;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ActiveRun extends BukkitRunnable {

  private Player player;

  private List<Vector> path;

  private final long start;

  public ActiveRun(Player player) {
    this.player = player;
    this.start = System.currentTimeMillis();
    this.path = new ArrayList<>();
  }

  public Record finish(RaceResult result) {
    this.cancel();
    RecordEntry entry = new RecordEntry(this.start, System.currentTimeMillis(), result, this.path);
    return new Record(player.getUniqueId(), entry);
  }

  @Override
  public void run() {
    this.path.add(player.getLocation().toVector());
  }

}
