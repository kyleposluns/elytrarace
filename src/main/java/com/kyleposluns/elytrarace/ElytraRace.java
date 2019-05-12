package com.kyleposluns.elytrarace;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.command.CreateArenaCommand;
import com.kyleposluns.elytrarace.command.ExitRaceCommand;
import com.kyleposluns.elytrarace.command.LoadArenaCommand;
import com.kyleposluns.elytrarace.command.RaceCommand;
import com.kyleposluns.elytrarace.config.ERConfig;
import com.kyleposluns.elytrarace.map.ArenaListener;
import com.kyleposluns.elytrarace.map.ArenaLoader;
import com.kyleposluns.elytrarace.map.ClipboardManager;
import com.kyleposluns.elytrarace.util.LocationTypeAdapter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;


public class ElytraRace extends JavaPlugin {

  private static ElytraRace instance;

  private Gson gson;

  private ERConfig config;

  private ArenaLoader arenaLoader;

  @Override
  public void onEnable() {
    instance = this;
    this.gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(Location.class, new LocationTypeAdapter())
            .create();
    this.saveDefaultConfig();

    this.config = new ERConfig(this.getConfig());
    this.arenaLoader = new ArenaLoader(this.config, this.getDataFolder(), this.gson);
    ClipboardManager clipboardManager = new ClipboardManager();
    this.getServer().getPluginManager().registerEvents(new ArenaListener(this.arenaLoader), this);
    this.getServer().getPluginManager().registerEvents(clipboardManager, this);
    this.getCommand("createarena").setExecutor(new CreateArenaCommand(this.arenaLoader,
            clipboardManager));
    this.getCommand("exitrace").setExecutor(new ExitRaceCommand(this.arenaLoader));
    this.getCommand("race").setExecutor(new RaceCommand(this.arenaLoader));
    this.getCommand("loadarena").setExecutor(new LoadArenaCommand(this.arenaLoader));
  }

  @Override
  public void onDisable() {
    instance = null;

  }

  public static ElytraRace getInstance() {
    return instance;
  }


}
