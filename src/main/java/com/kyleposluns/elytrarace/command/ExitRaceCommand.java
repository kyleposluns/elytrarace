package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.map.Arena;
import com.kyleposluns.elytrarace.map.ArenaLoader;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ExitRaceCommand extends ArenaCommand  {

  public ExitRaceCommand(ArenaLoader arenaLoader) {
    super(arenaLoader);
  }

  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 0) {
      return;
    }

    Optional<Arena> arenaOptional = this.arenaLoader.getArena(player);

    if (!arenaOptional.isPresent()) {
      failure(player, "You are not currently in a race!");
      return;
    }

    Arena arena = arenaOptional.get();

    arena.deactivatePlayer(player);

    success(player, "You have successfully exited the race!");

  }
}
