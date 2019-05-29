package com.kyleposluns.elytrarace.records;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class RecordEntry {

  private final long startTime;

  private final long endTime;

  private final RaceResult result;

  private final List<Vector> path;

  public RecordEntry() {
    this.startTime = System.currentTimeMillis();
    this.endTime = -1L;
    this.result = RaceResult.IN_PROGRESS;
    this.path = new ArrayList<>();
  }

  public RecordEntry(RecordEntry current, RaceResult result) {
    this.startTime = current.startTime;
    this.endTime = System.currentTimeMillis();
    this.result = result;
    this.path = current.getPath();
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
    if (this.result != RaceResult.IN_PROGRESS) {
      return Long.MAX_VALUE;
    }

    return this.endTime - this.startTime;
  }

  public List<Vector> getPath() {
    return this.path;
  }

}
