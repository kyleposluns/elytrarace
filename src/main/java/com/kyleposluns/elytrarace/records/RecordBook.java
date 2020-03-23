package com.kyleposluns.elytrarace.records;

import java.util.List;
import java.util.UUID;

/**
 * A RecordBook is a object that is responsible for keeping information about races called records.
 */
public interface RecordBook {

  /**
   * Find the top n records in this record book.
   *
   * @param n The amount of records to find.
   * @return The the top n records.
   */
  List<Record> getTopRecords(int n);

  /**
   * Find the top n records by a particular player.
   * @param n The top n records.
   * @param playerId The id of the player that is being searched.
   *
   */
  List<Record> getTopRecords(int n, UUID playerId);

  /**
   * Find the top record in this book. A shortcut for getTopRecords(1).get(0).
   */
  default Record getTopRecord() {
    return this.getTopRecords(1).get(0);
  }

}
