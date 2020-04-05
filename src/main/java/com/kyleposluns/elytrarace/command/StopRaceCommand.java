package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.game.RaceCoordinator;
import org.bukkit.entity.Player;

public class StopRaceCommand extends AbstractCommand {

  private RaceCoordinator coordinator;

  public StopRaceCommand(RaceCoordinator coordinator) {
    this.coordinator = coordinator;
  }

  @Override
  protected void onCommand(Player player, String[] args) {

    if (args.length != 0) {
      usage(player, "/stoprace");
      return;
    }

    this.coordinator.stopTracking(player.getUniqueId());
    success(player, "You are not being tracked anymore!");

  }
}
