package com.kyleposluns.elytrarace.database.sql.adapter;

import java.util.UUID;

public class PlayerRecordBookDeserializer extends RecordBookDeserializer {

  public PlayerRecordBookDeserializer(UUID id) {
    super(id);
  }

  @Override
  protected String getQueryProcedure() {
    return "get_player_record_book";
  }

}
