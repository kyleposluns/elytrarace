package com.kyleposluns.elytrarace.records;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

class LeaderboardRecordBook extends AbstractRecordBook {

  private final int maxEntries;

  LeaderboardRecordBook(File file, Gson gson, int maxEntries) throws FileNotFoundException {
    super(file, gson);
    this.maxEntries = maxEntries;
  }

  private void keepTopRecords() {
    Map<UUID, List<Record>> prunedRecords = new HashMap<>();

    List<Map.Entry<UUID, Record>> entries = this.recordMap.entrySet()
            .stream()
            .flatMap(uuidListEntry -> {
              List<Map.Entry<UUID, Record>> records = new ArrayList<>();
              for (Record record : uuidListEntry.getValue()) {
                records.add(new AbstractMap.SimpleEntry<>(uuidListEntry.getKey(), record));
              }
              return records.stream();
            })
            .sorted(Comparator.comparingLong(o -> o.getValue().getDuration()))
            .limit(this.maxEntries)
            .collect(Collectors.toList());

    for (Map.Entry<UUID, Record> entry : entries) {

      List<Record> records = prunedRecords.getOrDefault(entry.getKey(), new ArrayList<>());
      records.add(entry.getValue());

      prunedRecords.put(entry.getKey(), records);
    }
    this.recordMap = prunedRecords;
    this.saveData();
  }

  @Override
  protected void sanityCheck() {
    if (this.recordMap.values().size() > this.maxEntries && this.maxEntries > 0) {
      this.keepTopRecords();
    }
  }

}