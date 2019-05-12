package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.map.ArenaInfo;
import com.kyleposluns.elytrarace.map.ArenaLoader;
import com.kyleposluns.elytrarace.map.ClipboardManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateArenaCommand extends ArenaCommand {

  private ClipboardManager clipboardManager;

  private Map<UUID, ArenaInfo.Builder> arenaBuilders;

  public CreateArenaCommand(ArenaLoader loader, ClipboardManager manager) {
    super(loader);
    this.clipboardManager = manager;
    this.arenaBuilders = new HashMap<>();
  }
  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length == 2) {
      String name = args[0];

      String creator = args[1];

      ArenaInfo.Builder builder = new ArenaInfo.Builder();
      builder.name(name);
      builder.creator(creator);
      arenaBuilders.put(player.getUniqueId(), builder);
      player.sendMessage(ChatColor.GREEN + "Creating a new arena: " + name);
      return;
    }

    if (args.length == 1) {

      if (!arenaBuilders.containsKey(player.getUniqueId())) {
        player.sendMessage(ChatColor.RED + "There is no current arena being built!");
      }

      switch (args[0]) {

        case "spawn": {
          arenaBuilders.get(player.getUniqueId()).spawn(player.getLocation());
          success(player, "Spawn set at your current location!");
          break;
        }
        case "border": {
          arenaBuilders.get(player.getUniqueId()).border(clipboardManager.getClipboard(player.getUniqueId()).createArea());
          success(player, "A border was added!");
          break;
        }
        case "start": {
          arenaBuilders.get(player.getUniqueId()).start(clipboardManager.getClipboard(player.getUniqueId()).createArea());
          success(player, "Start area added!");
          break;
        }
        case "goal": {
          arenaBuilders.get(player.getUniqueId()).goal(clipboardManager.getClipboard(player.getUniqueId()).createArea());
          success(player, "Goal area Added!");
          break;
        }
        case "create": {
          ArenaInfo info = arenaBuilders.get(player.getUniqueId()).create();
          System.out.println(info.getName());
          try {
            this.arenaLoader.createArena(info);
            success(player, "Arena created successfully!");
          } catch (IOException e) {
            failure(player, "Something went wrong!");
            e.printStackTrace();
          }
          break;
        }

      }

    }
  }
}
