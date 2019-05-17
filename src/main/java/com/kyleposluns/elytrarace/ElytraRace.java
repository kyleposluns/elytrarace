package com.kyleposluns.elytrarace;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.config.ERConfig;
import org.bukkit.plugin.java.JavaPlugin;


public class ElytraRace extends JavaPlugin {

  private static ElytraRace instance;

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  private ERConfig config;

  private ArenaManager arenaManager;

  private String prefix;

  @Override
  public void onEnable() {
    instance = this;
    this.saveDefaultConfig();

    this.config = new ERConfig(this.getConfig());
    this.prefix = config.getPrefix();
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
