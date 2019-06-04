package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.ElytraRace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ArenaManagerImpl implements ArenaManager {

  private Map<UUID, Arena> activeArenas;

  private ArenaLookup arenaLookup;

  private File lookupFile;

  private File arenasDirectory;

  public ArenaManagerImpl(File arenasDirectory) throws IOException {
    this.arenasDirectory = arenasDirectory;
    this.activeArenas = new HashMap<>();
    this.initLookup();
    for (File arenaFile : Objects.requireNonNull(arenasDirectory.listFiles())) {
      String id = arenaFile.getName();
      File jsonFile = new File(arenaFile, id + ".json");
      ArenaInfo info = ElytraRace.getGson().fromJson(new FileReader(jsonFile), ArenaInfo.class);
      Arena arena = new ArenaImpl(info, arenaFile);
      this.activeArenas.put(info.getArenaId(), arena);
      ElytraRace.getInstance().getServer().getPluginManager().registerEvents(arena,
              ElytraRace.getInstance());
    }
  }

  private void initLookup() throws IOException {
    this.lookupFile = new File(arenasDirectory, "directory.json");
    if (!lookupFile.exists()) {
      this.arenaLookup = new ArenaLookup();
    } else {
      this.arenaLookup = ElytraRace.getGson().fromJson(new FileReader(this.lookupFile),
              ArenaLookup.class);
    }
  }

  private void saveArenaInfo(File arenaInfoFile, ArenaInfo info) throws IOException {
    FileOutputStream fos = new FileOutputStream(arenaInfoFile);
    String json = ElytraRace.getGson().toJson(info);
    fos.write(json.getBytes());
    fos.close();
  }

  private void saveLookup() throws IOException {
    FileOutputStream fos = new FileOutputStream(this.lookupFile);
    String json = ElytraRace.getGson().toJson(this.arenaLookup);
    fos.write(json.getBytes());
    fos.close();
  }

  @Override
  public void addArena(ArenaInfo info) {
    if (this.activeArenas.containsKey(info.getArenaId())) return;
    String id = info.getArenaId().toString();
    File arenaDirectory = new File(arenasDirectory, id);
    if (!arenaDirectory.exists()) {
      arenaDirectory.mkdir();
    }
    File arenaInfoFile = new File(arenaDirectory, id + ".json");
    if (!arenaInfoFile.exists()) {
      try {
        arenaInfoFile.createNewFile();

        Arena arena = new ArenaImpl(info, arenaDirectory);
        this.activeArenas.put(info.getArenaId(), arena);
        this.arenaLookup.put(info.getName(), info.getArenaId());


        this.saveArenaInfo(arenaInfoFile, info);
        this.saveLookup();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Optional<Arena> getArena(String name) {
    return Optional.ofNullable(this.activeArenas
            .get(this.arenaLookup.get(name)));
  }

  private class ArenaLookup {

    private Map<String, UUID> arenaNameMapping;

    ArenaLookup(Map<String, UUID> arenaNameMapping) {
      this.arenaNameMapping = arenaNameMapping;
    }

    ArenaLookup() {
      this(new HashMap<>());
    }

    UUID get(String name) {
      return this.arenaNameMapping.get(name);
    }

    void put(String name, UUID id) {
      this.arenaNameMapping.put(name, id);
    }

  }

}
