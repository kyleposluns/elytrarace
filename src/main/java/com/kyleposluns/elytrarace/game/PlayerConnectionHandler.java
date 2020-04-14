package com.kyleposluns.elytrarace.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {

  private RaceCoordinator coordinator;

  public PlayerConnectionHandler(RaceCoordinator coordinator) {
    this.coordinator = coordinator;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.coordinator.stopTracking(event.getPlayer().getUniqueId());
  }

}
