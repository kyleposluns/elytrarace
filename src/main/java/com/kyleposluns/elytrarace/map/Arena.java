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


  public boolean containsPlayer(Player player) {
    return this.activePlayers.contains(player.getUniqueId());
  }

  protected void addToPath(Player player, Location location) {
    if (this.paths.containsKey(player.getUniqueId())) {
      this.paths.get(player.getUniqueId()).getPath().add(location);
    }
  }

  public void lookupRecord(Consumer<RecordBook> func) {
    func.accept(this.recordBook);
  }

  public void lookupInfo(Consumer<ArenaInfo> func) {
    func.accept(this.info);
  }

  public void activatePlayer(Player player) {
    this.activePlayers.add(player.getUniqueId());
  }

  public void deactivatePlayer(Player player) {
    this.activePlayers.remove(player.getUniqueId());
    this.paths.remove(player.getUniqueId());
  } 

  public void startRun(Player player) {
    this.paths.put(player.getUniqueId(), new Record());
  }

  public Record finish(Player player, boolean finished) {
    Record record = new Record(this.paths.get(player.getUniqueId()), finished);
    this.recordBook.addRecord(player.getUniqueId(), record);
    this.paths.remove(player.getUniqueId());
    return record;
  }







}
