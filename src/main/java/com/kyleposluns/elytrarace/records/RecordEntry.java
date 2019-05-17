package com.kyleposluns.elytrarace.records;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class RecordEntry {

  private final long startTime;

  private final long endTime;

  private final boolean finished;

  private final List<Vector> path;

  public RecordEntry() {
    this.startTime = System.currentTimeMillis();
    this.endTime = -1L;
    this.finished = false;
    this.path = new ArrayList<>();
  }

  public RecordEntry(RecordEntry current, boolean finished) {
    this.startTime = current.startTime;
    this.endTime = System.currentTimeMillis();
    this.finished = finished;
    this.path = current.getPath();
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getFinishTime() {
    return this.endTime;
  }

  public boolean finished() {
    return this.finished;
  }

  public long getDuration() {
    if (!this.finished) {
      return Long.MAX_VALUE;
    }

    return this.endTime - this.startTime;
  }

  public List<Vector> getPath() {
    return this.path;
  }

}
