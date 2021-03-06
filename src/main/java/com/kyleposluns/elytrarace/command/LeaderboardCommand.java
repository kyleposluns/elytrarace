package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.records.RecordBookPrinter;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends AbstractCommand {

  private final ArenaManager arenaManager;

  public LeaderboardCommand(MessageFormatter formatter, String usage, ArenaManager arenaManager) {
    super(formatter, usage);
    this.arenaManager = arenaManager;

  }

  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 1) {
      usage(player);
      return;
    }

    String arenaName = args[0].toLowerCase();
    if (!this.arenaManager.hasArena(arenaName)) {
      failure(player, "Could not find an arena by the name " + arenaName);
      return;
    }




    try {

      Arena arena = this.arenaManager.getArena(arenaName);

      List<String> recordList = new RecordBookPrinter(this.messageFormatter)
          .apply(arena.getRecordBook());

      if (!recordList.isEmpty()) {
        this.messageFormatter
            .sendListMessage(player, String.format("Top records on %s!", arenaName), recordList);
      } else {
        this.messageFormatter.sendPrefixedMessage(player,
            ChatColor.YELLOW + String.format("There are not any records on the map %s.", arenaName));
      }
    } catch (Exception e) {
      e.printStackTrace();
      failure(player, "There was an issue viewing the leaderboard!\n" + e.getMessage());
    }


  }
}
