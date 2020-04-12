package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.game.RaceCoordinator;
import org.bukkit.entity.Player;

public class RaceCommand extends AbstractCommand {

  private RaceCoordinator raceCoordinator;

  public RaceCommand(MessageFormatter formatter, String usage, RaceCoordinator raceCoordinator) {
    super(formatter, usage);
    this.raceCoordinator = raceCoordinator;
  }

  @Override
  protected void onCommand(Player player, String[] args) {

    if (args.length != 1)  {
      usage(player);
      return;
    }

    String arena = args[0].toLowerCase();
    try {
      raceCoordinator.beginTracking(player.getUniqueId(), arena);
      success(player, String.format("Begin a race on %s!", arena));
    } catch (Exception e) {
      e.printStackTrace();
      failure(player, "There was an issue starting the race!\n" + e.getMessage());
    }
  }
}
