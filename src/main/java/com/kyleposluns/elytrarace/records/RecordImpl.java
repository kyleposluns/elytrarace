package com.kyleposluns.elytrarace.records;

import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

class RecordImpl implements Record {

  private final UUID playerId;

  private final UUID arenaId;

  private final long date;

  private final int time;

  RecordImpl(UUID playerId, UUID arenaId, long date, int time) {
    this.playerId = playerId;
    this.arenaId = arenaId;
    this.date = date;
    this.time = time;
  }

  @Override
  public UUID getPlayerId() {
    return this.playerId;
  }

  @Override
  public UUID getArenaId() {
    return this.arenaId;
  }

  @Override
  public long getDate() {
    return this.date;
  }

  @Override
  public int getTime() {
    return this.time;
  }

  @Override
  public int compareTo(Record o) {
    return Integer.compare(this.time, o.getTime());
  }
}
