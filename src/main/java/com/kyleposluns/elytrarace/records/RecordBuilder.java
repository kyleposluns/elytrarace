package com.kyleposluns.elytrarace.records;

import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class RecordBuilder {


  private UUID arenaId;

  private UUID playerId;

  private long date;

  private int time;

  private List<Vector> positions;

  public RecordBuilder() {
    this.arenaId = null;
    this.playerId = null;
    this.date = -1;
    this.time = -1;
    this.positions = null;
  }

  public RecordBuilder arenaId(UUID arenaId) {
    this.arenaId = arenaId;
    return this;
  }

  public RecordBuilder playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  public RecordBuilder date(long date) {
    this.date = date;
    return this;
  }

  public RecordBuilder time(int time) {
    this.time = time;
    return this;
  }

  public RecordBuilder positions(List<Vector> positions) {
    this.positions = positions;
    return this;
  }

  public Record build() {
    if (this.playerId == null || this.arenaId == null || this.positions == null || this.date == -1
        || this.time == -1) {
      throw new IllegalArgumentException("Cannot build a record with invalid information.");
    }
    return new RecordImpl(this.playerId, this.arenaId, this.date, this.time, this.positions);
  }

}
