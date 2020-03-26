package com.kyleposluns.elytrarace.records;

import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

class RecordImpl implements Record {

  private final UUID playerId;

  private final UUID arenaId;

  private final long date;

  private final int time;

  private final List<Vector> positions;

  RecordImpl(UUID playerId, UUID arenaId, long date, int time, List<Vector> positions) {
    this.playerId = playerId;
    this.arenaId = arenaId;
    this.date = date;
    this.time = time;
    this.positions = positions;
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
  public List<Vector> getPositions() {
    return List.copyOf(this.positions);
  }

  @Override
  public int compareTo(Record o) {
    return Integer.compare(this.time, o.getTime());
  }
}
