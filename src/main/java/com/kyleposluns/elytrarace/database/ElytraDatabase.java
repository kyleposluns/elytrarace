package com.kyleposluns.elytrarace.database;

import com.kyleposluns.elytrarace.arena.ArenaManager;
import com.kyleposluns.elytrarace.records.RecordBook;

public interface ElytraDatabase {

  ArenaManager getArenaManager();

  void saveRecordBook(RecordBook recordBook);


}
