package com.kyleposluns.elytrarace.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.util.UUID;

public abstract class AbstractDatabase implements ElytraDatabase {

  protected final Gson gson;

  public AbstractDatabase() {
    this.gson = new GsonBuilder().create();
  }

  protected abstract RecordBook findPlayerRecordBook(UUID playerId);

  protected abstract RecordBook findArenaRecordBook(UUID arenaId);

}
