package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

public class LeaderboardCommand extends AbstractCommand {

  private ArenaManager arenaManager;

  public LeaderboardCommand(MessageFormatter formatter, String usage, ArenaManager arenaManager) {
    super(formatter, usage);
    this.arenaManager = arenaManager;

  }

  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 1) {
      usage(player);
    }

    String arena = args[0].toLowerCase();
    try {
      List<String> recordList = arenaManager.getArena(arena).getRecordBook().getTopRecords(10)
          .stream().map(record -> {
            OfflinePlayer recordHolder = Bukkit.getOfflinePlayer(record.getPlayerId());
            String name = recordHolder.getName();
            if (name == null) {
              name = "null";
            }

            String dateTimeString =
                ChatColor.YELLOW + this.messageFormatter.formatTime(record.getTime())
                    + ChatColor.GRAY + " | " + ChatColor.YELLOW + this.messageFormatter
                    .formatDate(record.getDate());

            int dateTimeLength = ChatColor.stripColor(dateTimeString).length();
            int nameLength = name.length();
            int remaining = ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH - nameLength - dateTimeLength;
            char[] spaces = new char[remaining];
            Arrays.fill(spaces, ' ');
            String space = new String(spaces);

            return name + ChatColor.GRAY + ":" + space + ChatColor.YELLOW + this.messageFormatter
                .formatTime(record.getTime())
                + ChatColor.GRAY + " | " + ChatColor.YELLOW + this.messageFormatter
                .formatDate(record.getDate());
          }).collect(Collectors.toList());

      this.messageFormatter
          .sendListMessage(player, String.format("Top records on %s!", arena), recordList);
    } catch (Exception e) {
      e.printStackTrace();
      failure(player, "There was an issue viewing the leaderboard!\n" + e.getMessage());
    }


  }
}
