package com.kyleposluns.elytrarace;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.config.ERConfig;
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
    ClipboardManager clipboardManager = new ClipboardManager();

    this.config = new ERConfig(this.getConfig());
    this.arenaLoader = new ArenaLoader(this.config, this.getDataFolder(), this.gson);
  }

  @Override
  public void onDisable() {
    instance = null;

  }

  public static ElytraRace getInstance() {
    return instance;
  }


}
