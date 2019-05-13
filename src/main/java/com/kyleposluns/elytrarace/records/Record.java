package com.kyleposluns.elytrarace.records;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public final class Record {

  private final long startTime;

  private final long endTime;

  private final boolean finished;

  private final List<Location> path;

  public Record() {
    this.startTime = System.currentTimeMillis();
    this.endTime = -1L;
    this.finished = false;
    this.path = new ArrayList<>();
  }

  public Record(Record current, boolean finished) {
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

  public List<Location> getPath() {
    return this.path;
  }

}
