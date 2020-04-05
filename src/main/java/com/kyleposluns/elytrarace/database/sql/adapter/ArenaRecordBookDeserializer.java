package com.kyleposluns.elytrarace.database.sql.adapter;

import java.util.UUID;

public class ArenaRecordBookDeserializer extends RecordBookDeserializer {

  public ArenaRecordBookDeserializer(UUID id) {
    super(id);
  }

  @Override
  protected String getQueryProcedure() {
    return "get_arena_record_book";
  }

}
