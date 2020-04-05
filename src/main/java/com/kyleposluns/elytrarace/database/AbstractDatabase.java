package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.records.RecordBook;
import java.util.UUID;

public abstract class AbstractDatabase implements ElytraDatabase {

  protected final Credentials credentials;

  public AbstractDatabase(Credentials credentials) {
    this.credentials = credentials;
  }

  protected abstract RecordBook findPlayerRecordBook(UUID playerId);

  protected abstract RecordBook findArenaRecordBook(UUID arenaId);

}
