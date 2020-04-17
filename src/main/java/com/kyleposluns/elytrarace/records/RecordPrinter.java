package com.kyleposluns.elytrarace.records;

import com.kyleposluns.elytrarace.MessageFormatter;
import java.util.Arrays;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.ChatPaginator;

public class RecordPrinter implements Function<Record, String> {

  private final MessageFormatter formatter;

  public RecordPrinter(MessageFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public String apply(Record record) {
    OfflinePlayer recordHolder = Bukkit.getOfflinePlayer(record.getPlayerId());
    String name = recordHolder.getName();
    if (name == null) {
      name = "null";
    }

    String dateTimeString =
        ChatColor.YELLOW + this.formatter.formatTime(record.getTime())
            + ChatColor.GRAY + " | " + ChatColor.YELLOW + this.formatter
            .formatDate(record.getDate());

    int dateTimeLength = ChatColor.stripColor(dateTimeString).length();
    int nameLength = name.length();
    int remaining = ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH - nameLength - dateTimeLength;
    char[] spaces = new char[remaining];
    Arrays.fill(spaces, ' ');
    String space = new String(spaces);

    return name + ChatColor.GRAY + ":" + space + ChatColor.YELLOW + this.formatter
        .formatTime(record.getTime())
        + ChatColor.GRAY + " | " + ChatColor.YELLOW + this.formatter
        .formatDate(record.getDate());
  }
}
