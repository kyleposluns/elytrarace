package com.kyleposluns.elytrarace;


import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.command.ArenasCommand;
import com.kyleposluns.elytrarace.command.LeaderboardCommand;
import com.kyleposluns.elytrarace.command.RaceCommand;
import com.kyleposluns.elytrarace.command.StopRaceCommand;
import com.kyleposluns.elytrarace.config.ERConfig;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.game.PlayerConnectionHandler;
import com.kyleposluns.elytrarace.game.RaceCoordinator;
import com.kyleposluns.elytrarace.game.RaceCoordinatorImpl;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class ElytraRace extends JavaPlugin {

  private RaceCoordinator raceCoordinator;

  private ElytraDatabase database;

  private ArenaManager arenaManager;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    ERConfig config = new ERConfig(this, this.getConfig());
    MessageFormatter messageFormatter = config.getMessageFormatter();
    this.database = config.getDatabase();
    this.arenaManager = this.database.getArenaManager();
    this.raceCoordinator = new RaceCoordinatorImpl(this.arenaManager);

    PluginCommand raceCommand = this.getCommand("race");
    if (raceCommand != null) {
      raceCommand.setExecutor(
          new RaceCommand(messageFormatter, raceCommand.getUsage(), this.raceCoordinator));
    }

    PluginCommand stopRaceCommand = this.getCommand("stoprace");

    if (stopRaceCommand != null) {
      stopRaceCommand
          .setExecutor(new StopRaceCommand(messageFormatter, stopRaceCommand.getUsage(),
              this.raceCoordinator));
    }

    PluginCommand arenasCommand = this.getCommand("arenas");

    if (arenasCommand != null) {
      arenasCommand.setExecutor(
          new ArenasCommand(messageFormatter, arenasCommand.getUsage(), this.arenaManager));
    }

    PluginCommand leaderboardCommand = this.getCommand("leaderboard");
    if (leaderboardCommand != null) {
      leaderboardCommand
          .setExecutor(new LeaderboardCommand(messageFormatter, leaderboardCommand.getUsage(),
              this.arenaManager));
    }
    this.getServer().getPluginManager()
        .registerEvents(new PlayerConnectionHandler(this.raceCoordinator), this);

  }

  @Override
  public void onDisable() {
    this.raceCoordinator.untrackAll();
    this.arenaManager.getLoadedArenas()
        .forEach(s -> this.database.saveRecordBook(arenaManager.getArena(s).getRecordBook()));
    this.database.close();
    HandlerList.unregisterAll(this);
  }

}
