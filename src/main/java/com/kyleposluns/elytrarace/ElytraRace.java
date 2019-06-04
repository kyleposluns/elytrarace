package com.kyleposluns.elytrarace;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.config.ERConfig;
import com.kyleposluns.elytrarace.map.ArenaManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ElytraRace extends JavaPlugin {

  private static ElytraRace instance;

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  private static final String ARENA_DIRECTORY_PATH = "arenas";

  private ERConfig config;

  private ArenaManagerImpl arenaManager;

  private String prefix;

  @Override
  public void onEnable() {
    instance = this;
    this.saveDefaultConfig();

    this.config = new ERConfig(this.getConfig());
    this.prefix = config.getPrefix();
    File arenasDir = new File(this.getDataFolder(), ARENA_DIRECTORY_PATH);
    if (!arenasDir.exists()) {
      arenasDir.mkdirs();
    }
    this.arenaManager = new ArenaManagerImpl(arenasDir);
  }

  public ERConfig getGameConfig() {
    return this.config;
  }


  @Override
  public void onDisable() {
    instance = null;
  }

  public String getPrefixedMessage(String message) {
    return this.prefix + " " + message;
  }


  public static Gson getGson() {
    return GSON;
  }

  public static ElytraRace getInstance() {
    return instance;
  }


}
