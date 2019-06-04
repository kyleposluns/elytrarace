package com.kyleposluns.elytrarace.records;

import org.bukkit.util.Vector;

import java.util.List;

public final class RecordEntry {

  private final long startTime;

  private final long endTime;

  private final RaceResult result;

  private final List<Vector> path;

  RecordEntry(long startTime, long endTime, RaceResult result, List<Vector> path) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.result = result;
    this.path = path;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getFinishTime() {
    return this.endTime;
  }

  public RaceResult finished() {
    return this.result;
  }

  public long getDuration() {
    if (this.result != RaceResult.SUCCESS) {
      return Long.MAX_VALUE;
    }

    return this.endTime - this.startTime;
  }

  public List<Vector> getPath() {
    return this.path;
  }

}
