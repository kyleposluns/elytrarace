package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {

  protected MessageFormatter messageFormatter;

  private String usage;

  public AbstractCommand(MessageFormatter messageFormatter, String usage) {
    this.messageFormatter = messageFormatter;
    this.usage = usage;
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


  protected void usage(CommandSender sender) {
   sender.sendMessage(this.messageFormatter.getPrefix() + ChatColor.YELLOW + this.usage);
  }

  protected void success(CommandSender sender, String message) {
    sender.sendMessage(this.messageFormatter.getPrefix() + ChatColor.GREEN + message);
  }

  protected void failure(CommandSender sender, String message) {
    sender.sendMessage(this.messageFormatter.getPrefix() + ChatColor.RED + message);  }

}
