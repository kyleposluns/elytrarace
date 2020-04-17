package com.kyleposluns.elytrarace.game;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {

  private final RaceCoordinator coordinator;

  public PlayerConnectionHandler(RaceCoordinator coordinator) {
    this.coordinator = coordinator;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.coordinator.stopTracking(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
  }

}