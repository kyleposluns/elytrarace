package com.kyleposluns.elytrarace.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ERConfig {

  private static final String PREFIX = "prefix";

  private static final String MAX_RECORD_ENTRIES = "records.max_entries";

  private FileConfiguration config;

  public ERConfig(FileConfiguration config) {
    this.config = config;
  }

  public String getPrefix() {
    return ChatColor.translateAlternateColorCodes('&',
            this.config.getString(PREFIX, ""));

  }
  public int getMaxEntries() {
    return this.config.getInt(MAX_RECORD_ENTRIES);
  }





}
