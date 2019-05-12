package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.map.Arena;
import com.kyleposluns.elytrarace.map.ArenaLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class RaceCommand extends ArenaCommand {

  public RaceCommand(ArenaLoader arenaLoader) {
    super(arenaLoader);
  }


  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 1) {
      return;
    }

    String arenaName = args[0];

    Optional<Arena> arenaOptional = this.arenaLoader.getArena(arenaName.toLowerCase());

    if (!arenaOptional.isPresent()) {
      failure(player, "Cannot find an arena by the name: " + arenaName);
      return;
    }


    Arena arena = arenaOptional.get();
    arena.lookupInfo(info -> {
      player.teleport(info.getSpawn());
      player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
    });
    arena.activatePlayer(player);
    success(player, "To begin the race: jump off of the start!");

  }
}
