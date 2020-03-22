package com.kyleposluns.elytrarace;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ElytraRace extends JavaPlugin {

  private static ElytraRace instance;

  private static final String ARENA_DIRECTORY_PATH = "arenas";

  private String prefix;

  @Override
  public void onEnable() {
    instance = this;
    this.saveDefaultConfig();

  }


  @Override
  public void onDisable() {
    instance = null;
  }



}
