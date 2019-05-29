package com.kyleposluns.elytrarace.records;

import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecordBook {

  List<Vector> getFastestPath();

  Optional<PlayerDataEntry> getPlayerEntry(UUID owner);

  List<Record> getTopRecords();

  void save();

  void addPlayer(UUID playerId);

  void addRecord(Record record);

}
