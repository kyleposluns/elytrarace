package com.kyleposluns.elytrarace.config;

import org.bukkit.configuration.file.FileConfiguration;

public class ERConfig {

  private static final String MAX_RECORD_ENTRIES = "records.max_entries";

  private static final String RECORD_DURATION = "records.duration";

  private static final String PLAYER_DATA = "arenas.player_record_dir";

  private static final String ARENA_DATA = "arenas.data_file";

  private FileConfiguration config;

  public ERConfig(FileConfiguration config) {
    this.config = config;
  }

  public String getPlayerData() {
    return this.config.getString(PLAYER_DATA);
  }

  public String getArenaData() {
    return this.config.getString(ARENA_DATA);
  }

  public int getMaxEntries() {
    return this.config.getInt(MAX_RECORD_ENTRIES);
  }

  public int getRecordDuration() {
    return this.config.getInt(RECORD_DURATION);
  }




}
