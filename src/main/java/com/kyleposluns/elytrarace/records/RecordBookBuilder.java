package com.kyleposluns.elytrarace.records;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class RecordBookBuilder {

  private final List<Record> records;

  public RecordBookBuilder() {
    this.records = new ArrayList<>();
  }

  public RecordBookBuilder record(Record record) {
    this.records.add(record);
    return this;
  }

  public RecordBookBuilder record(UUID playerId, UUID arenaId, long date, int time,
      List<Vector> positions) {
    this.records.add(new RecordImpl(playerId, arenaId, date, time, positions));
    return this;
  }

  public RecordBookBuilder records(List<Record> records) {
    this.records.addAll(records);
    return this;
  }

  public RecordBookBuilder records(RecordBook book) {
    for (Record record : book) {
      this.records.add(record);
    }
    return this;
  }

  public RecordBook build() {
    return new RecordBookImpl(this.records);
  }

}
