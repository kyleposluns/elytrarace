package com.kyleposluns.elytrarace;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

public class MessageFormatter {

  private ChatColor primary;

  private ChatColor secondary;

  private ChatColor alternate;

  private DateFormat timeFormat;

  private DateFormat dateFormat;

  private String prefix;

  public MessageFormatter(ChatColor primary, ChatColor secondary, ChatColor alternate) {
    this.primary = primary;
    this.secondary = secondary;
    this.alternate = alternate;
    this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    this.timeFormat = new SimpleDateFormat("mm:ss:SSS");
    this.prefix = this.getPrefix();
  }

  public String formatDate(long date) {
    return this.dateFormat.format(new Date(date));
  }

  public String formatTime(long time) {
    return this.timeFormat.format(new Date(time));
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

    int titleLength = ChatColor.stripColor(title).length();

    int topFreeSpace = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - titleLength;
    int topEqualsSpace = topFreeSpace / 2;
    char[] topEquals = new char[topEqualsSpace];
    Arrays.fill(topEquals, '=');

    StringBuilder builder = new StringBuilder()
        .append(this.secondary).append("[")
        .append(this.alternate)
        .append(topEquals)
        .append(this.primary)
        .append(title)
        .append(this.alternate)
        .append(topEquals)
        .append(this.secondary).append("]")
        .append("\n");
    int bottomFreeSpace = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - 3;
    char[] bottomEquals = new char[bottomFreeSpace];
    Arrays.fill(bottomEquals, '=');

    for (int i = 0; i < lines.size(); i++) {
      String arena = lines.get(i);
      builder.append(this.primary).append(i + 1).append(". ").append(this.secondary).append(arena);
        builder.append("\n");
    }

    builder
        .append(this.secondary).append("[")
        .append(this.alternate)
        .append(bottomEquals)
        .append(this.secondary).append("]")
        .append("\n");
    return builder.toString();
  }

}
