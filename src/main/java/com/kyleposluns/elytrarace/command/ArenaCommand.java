package com.kyleposluns.elytrarace.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ArenaCommand implements CommandExecutor {


  public ArenaCommand() {

  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      failure(sender, "You must be a player to execute this command!");
      return true;
    }

    try {
      this.onCommand((Player) sender, args);
    } catch (Exception e) {
      failure(sender, "Something went wrong!");
      e.printStackTrace();
    }

    return true;
  }

  protected abstract void onCommand(Player player, String[] args);


  protected void usage(CommandSender sender, String message) {
   sender.sendMessage(ChatColor.YELLOW + message);
  }

  protected void success(CommandSender sender, String message) {
    sender.sendMessage(ChatColor.GREEN + message);
  }

  protected void failure(CommandSender sender, String message) {
    sender.sendMessage(ChatColor.RED + message);
  }

}
