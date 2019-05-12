package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.map.ArenaLoader;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;

public class LoadArenaCommand extends ArenaCommand {


  public LoadArenaCommand(ArenaLoader arenaLoader) {
    super(arenaLoader);
  }

  @Override
  protected void onCommand(Player player, String[] args) {

    if (args.length != 1) {
      return;
    }

    String name = args[0];

    try {
      this.arenaLoader.loadArena(name);
      success(player, "Successfully loaded arena: " + name);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
