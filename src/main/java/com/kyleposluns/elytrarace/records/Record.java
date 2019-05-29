package com.kyleposluns.elytrarace.records;

import java.util.UUID;

public final class Record {

  private final UUID recordId;

  private final RecordEntry entry;

  public Record(UUID recordId, RecordEntry entry) {
    this.recordId = recordId;
    this.entry = entry;
  }

  protected RecordEntry getRaw() {
    return this.entry;
  }


  public UUID getRecordId() {
    return this.recordId;
  }

}
