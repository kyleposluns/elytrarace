package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.records.RecordBook;
import java.util.UUID;

public interface ElytraDatabase {

  ArenaManager getArenaManager();

  void saveRecordBook(RecordBook recordBook);

  RecordBook getPlayerRecords(UUID playerId);

  void close();

}
