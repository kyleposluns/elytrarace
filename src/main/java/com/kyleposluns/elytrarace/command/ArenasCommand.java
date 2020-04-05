package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.arena.ArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenasCommand extends AbstractCommand {

  private ArenaManager arenaManager;

  public ArenasCommand(ArenaManager arenaManager) {
    this.arenaManager = arenaManager;
  }

  private String getFormattedString() {
    StringBuilder builder = new StringBuilder()
        .append(ChatColor.GRAY).append("[")
        .append(ChatColor.DARK_AQUA)
        .append("=======================================")
        .append(ChatColor.GRAY).append("]")
        .append("\n");

    for (int i = 0; i < this.arenaManager.getLoadedArenas().size(); i++) {
      String arena = this.arenaManager.getLoadedArenas().get(i);
      builder.append(ChatColor.DARK_BLUE).append(i + 1).append(". ").append(ChatColor.BLUE)
          .append(arena).append("\n");
    }
    builder.append(ChatColor.GRAY).append("[")
        .append(ChatColor.DARK_AQUA)
        .append("=======================================")
        .append(ChatColor.GRAY).append("]")
        .append("\n");
    builder.append(ChatColor.RESET);
    return builder.toString();
  }

  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 0) {
      usage(player, "/arenas");
      return;
    }
    player.sendMessage(getFormattedString());
  }
}
