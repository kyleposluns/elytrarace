package com.kyleposluns.elytrarace.map;

import com.google.gson.Gson;
import com.kyleposluns.elytrarace.config.ERConfig;
import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.util.ERUtils;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArenaLoader {

  private ERConfig config;

  private File arenaRoot;

  private List<Arena> activeArenas;

  private Gson gson;

  public ArenaLoader(ERConfig config, File dataFolder, Gson gson) {
    this.arenaRoot = new File(dataFolder,"arenas");
    if (!arenaRoot.exists()) {
      arenaRoot.mkdir();
    }
    this.config = config;
    this.gson = gson;
    this.activeArenas = new ArrayList<>();
  }

  public Optional<Arena> getArena(String name) {
    return activeArenas.stream().filter(arena -> arena.info.getName().equals(name)).findFirst();
  }

  public boolean isArenaLoaded(String name) {
    return activeArenas.stream().anyMatch(arena -> arena.info.getName().equals(name));
  }

  public Optional<Arena> getArena(Player player) {
    return activeArenas.stream().filter(arena -> arena.containsPlayer(player)).findFirst();

  }

  public void createArena(ArenaInfo arenaInfo) throws IOException {
    File arenaDir = new File(arenaRoot, arenaInfo.getName());

    if (arenaDir.exists()) {
      throw new IllegalArgumentException("The arena " + arenaInfo.getName() + " already exists!");
    }

    arenaDir.mkdir();

    File arenaData = new File(arenaDir, config.getArenaData());

    if (!(arenaData.exists())) {
      arenaData.createNewFile();
      FileOutputStream fos = new FileOutputStream(arenaData);
      fos.write(this.gson.toJson(arenaInfo, ArenaInfo.class).getBytes());
      fos.close();
    }

    File playerData = new File(arenaDir, config.getPlayerData());
    if (!playerData.exists()) {
      playerData.mkdir();
    }

    RecordBook.Builder recordBookBuilder = new RecordBook.Builder();
    recordBookBuilder.setDirectory(playerData);
    recordBookBuilder.setGson(this.gson);
    recordBookBuilder.setDurationUntilExpiration(config.getRecordDuration());
    recordBookBuilder.setMaxEntries(config.getMaxEntries());
    RecordBook recordBook = recordBookBuilder.create();
    recordBook.saveData();
  }

  public Arena loadArena(String name) throws FileNotFoundException {

    if (isArenaLoaded(name)) {
      throw new IllegalArgumentException("Arena " + name + " is already loaded!");
    }

    File arenaDir = new File(arenaRoot, name);

    if (!arenaDir.exists()) {
      throw new FileNotFoundException("Cannot find a directory for the arena " + name);
    }

    File arenaData = new File(arenaDir, config.getArenaData());

    if (!arenaData.exists()) {
      throw new FileNotFoundException("Cannot find arena data for the arena " + name);
    }

    File playerData = new File(arenaDir, config.getPlayerData());
    if (!playerData.exists()) {
      playerData.mkdir();
    }

    RecordBook.Builder recordBookBuilder = new RecordBook.Builder();
    recordBookBuilder.setDirectory(playerData);
    recordBookBuilder.setGson(this.gson);
    recordBookBuilder.setDurationUntilExpiration(config.getRecordDuration());
    recordBookBuilder.setMaxEntries(config.getMaxEntries());
    RecordBook recordBook = recordBookBuilder.create();

    ArenaInfo arenaInfo = this.gson.fromJson(new FileReader(arenaData), ArenaInfo.class);
    Arena arena = new Arena(arenaInfo, recordBook);
    this.activeArenas.add(arena);
    return arena;
  }

  private void unloadArena(Arena arena) {
    arena.recordBook.saveData();

    String json = this.gson.toJson(arena.info);
    File arenaDir = new File(this.arenaRoot, arena.info.getName());

    try {
      FileOutputStream fos = new FileOutputStream(new File(arenaDir,
              config.getArenaData()));
      fos.write(json.getBytes());
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void unloadArena(String name) {
    Optional<Arena> arenaOptional = getArena(name);
    if (arenaOptional.isPresent()) {
      Arena arena = arenaOptional.get();
      unloadArena(arena);
    } else {
      throw new IllegalArgumentException("Cannot find the loaded arena " + name);
    }
  }

  public void loadAll() throws FileNotFoundException {
    for (File file : Objects.requireNonNull(this.arenaRoot.listFiles())) {
      if (file.isDirectory()) {
        loadArena(ERUtils.stripFileExtension(file.getName()));
      }

    }
  }

  public void unloadAll() {
    this.activeArenas.forEach(this::unloadArena);
  }

}
