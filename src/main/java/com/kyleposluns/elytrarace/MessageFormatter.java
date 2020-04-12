package com.kyleposluns.elytrarace;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageFormatter {

  private ChatColor primary;

  private ChatColor secondary;

  private ChatColor alternate;

  private String prefix;

  public MessageFormatter(ChatColor primary, ChatColor secondary, ChatColor alternate) {
    this.primary = primary;
    this.secondary = secondary;
    this.alternate = alternate;
    this.prefix = this.getPrefix();
  }

  public String getPrefix() {
    return this.alternate + "[" + this.primary + "ElytraRace" + this.alternate + "] " + ChatColor.RESET;
  }

  public void sendPrefixedMessage(Player player, String message) {
    player.sendMessage(this.prefix + message);
  }

  public void sendListMessage(Player player, String title, List<String> lines) {
    player.sendMessage(this.getListMessage(title, lines));
  }

  public String getListMessage(String title, List<String> lines) {
    StringBuilder builder = new StringBuilder()
        .append(this.secondary).append("[")
        .append(this.alternate)
        .append("=============")
        .append(this.primary)
        .append(title)
        .append(this.alternate)
        .append("=============")
        .append(this.secondary).append("]")
        .append("\n");
    char[] chars = new char[title.length()];
    for (int i = 0; i < title.length(); i++) {
      chars[i] = '=';
    }

    for (int i = 0; i < lines.size(); i++) {
      String arena = lines.get(i);
      builder.append(this.primary).append(i + 1).append(". ").append(this.secondary).append(arena);
        builder.append("\n");
    }

    builder
        .append(this.secondary).append("[")
        .append(this.alternate)
        .append("=============")
        .append(chars)
        .append("=============")
        .append(this.secondary).append("]")
        .append("\n");
    return builder.toString();
  }

}
