package com.kyleposluns.elytrarace.command;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.arena.ArenaManager;
import org.bukkit.entity.Player;

public class ArenasCommand extends AbstractCommand {

  private final ArenaManager arenaManager;

  public ArenasCommand(MessageFormatter formatter, String usage, ArenaManager arenaManager) {
    super(formatter, usage);
    this.arenaManager = arenaManager;
  }


  @Override
  protected void onCommand(Player player, String[] args) {
    if (args.length != 0) {
      usage(player);
      return;
    }

    this.messageFormatter.sendListMessage(player, "Available Arenas", this.arenaManager.getArenaDisplayNames());
  }
}
