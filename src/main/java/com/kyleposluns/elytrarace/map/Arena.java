package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordBook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class Arena {

  protected final RecordBook recordBook;

  protected final ArenaInfo info;

  private Set<UUID> activePlayers;

  private Map<UUID, Record> paths;

  Arena(ArenaInfo info, RecordBook recordBook) {
    this.info = info;
    this.recordBook = recordBook;
    this.activePlayers = new HashSet<>();
    this.paths = new HashMap<>();
  }





}
