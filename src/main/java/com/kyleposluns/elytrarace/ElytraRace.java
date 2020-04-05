package com.kyleposluns.elytrarace;


import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.config.ERConfig;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.game.RaceCoordinator;
import com.kyleposluns.elytrarace.game.RaceCoordinatorImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class ElytraRace extends JavaPlugin {

  private RaceCoordinator raceCoordinator;

  private ElytraDatabase database;

  private ArenaManager arenaManager;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    ERConfig config = new ERConfig(this.getConfig());
    this.database = config.getDatabase();
    this.arenaManager = this.database.getArenaManager();
    this.raceCoordinator = new RaceCoordinatorImpl(this, this.database, this.arenaManager);
  }


  @Override
  public void onDisable() {
    this.raceCoordinator.untrackAll();
    this.arenaManager.getLoadedArenas()
        .forEach(s -> this.database.saveRecordBook(arenaManager.getArena(s).getRecordBook()));
    this.database.close();

  }


}
