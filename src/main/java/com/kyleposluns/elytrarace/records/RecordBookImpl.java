package com.kyleposluns.elytrarace.records;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class RecordBookImpl implements RecordBook {

  private final List<Record> records;


  RecordBookImpl(List<Record> records) {
    this.records = records;
  }


  @Override
  public List<Record> getTopRecords(int n) {
    return this.records.stream().sorted().limit(n).collect(Collectors.toList());
  }

  @Override
  public List<Record> getTopRecords(int n, UUID playerId) {
    return this.records.stream().filter(record -> record.getPlayerId().equals(playerId)).sorted()
        .limit(n).collect(
            Collectors.toList());
  }

  @Override
  public void report(Record record) {
    this.records.add(record);
  }

  @Override
  public Iterator<Record> iterator() {
    return this.records.iterator();
  }


}
