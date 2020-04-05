package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.game.RaceCoordinator;
import org.bukkit.entity.Player;

public class RaceCommand extends AbstractCommand {

  private RaceCoordinator raceCoordinator;

  public RaceCommand(RaceCoordinator raceCoordinator) {
    this.raceCoordinator = raceCoordinator;
  }

  @Override
  protected void onCommand(Player player, String[] args) {

    if (args.length != 1)  {
      usage(player, "/race <arena>");
    }

    String arena = args[0].toLowerCase();
    try {
      raceCoordinator.beginTracking(player.getUniqueId(), arena);
      success(player, String.format("Begin a race on %s!", arena));
    } catch (Exception e) {
      failure(player, "There was an issue starting the race!");
    }

  }
}
