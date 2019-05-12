package com.kyleposluns.elytrarace.map;

import org.bukkit.entity.Player;

public class PlayerTracker implements Runnable {

  private Arena arena;

  private Player player;

  public PlayerTracker(Player player, Arena arena) {
    this.player = player;
    this.arena = arena;
  }


  @Override
  public void run() {
    this.arena.addToPath(player, player.getLocation());
    System.out.println("Test Line 20");
  }
}
