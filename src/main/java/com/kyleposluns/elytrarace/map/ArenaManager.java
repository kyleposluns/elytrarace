package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.ElytraRace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaManager {

  private List<Arena> activeArenas;

  private File arenasDirectory;

  public ArenaManager(File arenasDirectory) throws FileNotFoundException {
    this.arenasDirectory = arenasDirectory;
    this.activeArenas = new ArrayList<>();
    for (File arenaFile : Objects.requireNonNull(arenasDirectory.listFiles())) {
      String id = arenaFile.getName();
      File jsonFile = new File(arenaFile, id + ".json");
      ArenaInfo info = ElytraRace.getGson().fromJson(new FileReader(jsonFile), ArenaInfo.class);
      Arena arena = new ArenaImpl(info, arenaFile);
      this.activeArenas.add(arena);
      ElytraRace.getInstance().getServer().getPluginManager().registerEvents(arena, ElytraRace.getInstance());
    }
  }






}
