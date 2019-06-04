package com.kyleposluns.elytrarace.records;

import java.util.LinkedList;
import java.util.List;

public class PlayerDataEntry {

  private final List<RecordEntry> records;

  public PlayerDataEntry() {
    this.records = new LinkedList<>();
  }

  public List<RecordEntry> getRecords() {
    return this.records;
  }

}
