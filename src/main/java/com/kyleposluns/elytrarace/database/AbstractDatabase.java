package com.kyleposluns.elytrarace.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleposluns.elytrarace.arena.area.AreaTypeAdapterFactory;
import com.kyleposluns.elytrarace.arena.LocationAdapter;
import com.kyleposluns.elytrarace.records.Record;
import com.kyleposluns.elytrarace.records.RecordAdapter;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.util.UUID;
import org.bukkit.Location;

public abstract class AbstractDatabase implements ElytraDatabase {

  protected final Gson gson;

  protected final Credentials credentials;

  public AbstractDatabase(Credentials credentials) {
    this.credentials = credentials;
    this.gson = new GsonBuilder().registerTypeAdapter(Record.class, new RecordAdapter())
        .registerTypeAdapter(Location.class, new LocationAdapter())
        .registerTypeAdapterFactory(new AreaTypeAdapterFactory()).create();
  }

  protected abstract RecordBook findPlayerRecordBook(UUID playerId);

  protected abstract RecordBook findArenaRecordBook(UUID arenaId);

}
