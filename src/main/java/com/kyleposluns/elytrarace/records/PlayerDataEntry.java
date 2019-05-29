package com.kyleposluns.elytrarace.records;

import com.kyleposluns.elytrarace.util.ERUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PlayerDataEntry {

  private static final Comparator<RecordEntry> ENTRY_COMPARATOR =
          Comparator.comparingLong(RecordEntry::getDuration);

  private static final int ENTRY_LIMIT = 5;

  private final int maxTop;

  private final Comparator<RecordEntry> entryComparator;

  private final LinkedList<RecordEntry> records;

  public PlayerDataEntry() {
    this.records = new LinkedList<>();
    this.maxTop = ENTRY_LIMIT;
    this.entryComparator = ENTRY_COMPARATOR;
  }

  public void addEntry(RecordEntry entry) {
    ERUtils.insert(this.records, entry, this.entryComparator, this.maxTop);
  }

  public List<RecordEntry> getRecords() {
    return this.records;
  }


}
