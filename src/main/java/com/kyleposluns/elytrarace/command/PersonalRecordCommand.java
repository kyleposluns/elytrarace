package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.Arena;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.records.RecordPrinter;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PersonalRecordCommand extends AbstractCommand {

  private final ArenaManager arenaManager;

  public PersonalRecordCommand(MessageFormatter messageFormatter,
      String usage, ArenaManager arenaManager) {
    super(messageFormatter, usage);
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

      List<String> recordList = arena.getRecordBook()
          .getTopRecords(10, player.getUniqueId()).stream()
          .map(record -> new RecordPrinter(this.messageFormatter).apply(record)).collect(
              Collectors.toList());

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
