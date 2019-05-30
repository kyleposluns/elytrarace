package com.kyleposluns.elytrarace.records;

import java.util.UUID;

public final class Record {

  private final UUID playerId;

  private final RecordEntry entry;

  public Record(UUID recordId, RecordEntry entry) {
    this.playerId = recordId;
    this.entry = entry;
  }

  protected RecordEntry getRaw() {
    return this.entry;
  }


  public UUID getUniqueId() {
    return this.playerId;
  }

}
