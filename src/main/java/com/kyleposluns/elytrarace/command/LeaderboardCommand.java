package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
            return name;
          }).collect(Collectors.toList());

      this.messageFormatter.sendListMessage(player, String.format("Top records on %s!", arena), recordList);
    } catch (Exception e) {
      e.printStackTrace();
      failure(player, "There was an issue viewing the leaderboard!\n" + e.getMessage());
    }


  }
}
