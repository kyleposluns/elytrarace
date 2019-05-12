package com.kyleposluns.elytrarace.records;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface RecordBook {

  List<Record> getTopRecords(int n);

  List<Record> getTopRecords(UUID recordHolder, int n);

  List<Record> getRecentRecords(int n);

  List<Record> getRecentRecords(UUID recordHolder, int n);

  double getSuccessRate(UUID recordHolder);

  double getFailRate(UUID recordHolder);

  void addRecord(UUID recordHolder, Record record);

  void saveData();

  class Builder {


    private int maxEntries;

    private long durationUntilExpiration;

    private File root;

    private Gson gson;


    public Builder() {
      this.maxEntries = -1;
      this.durationUntilExpiration = -1;
    }

    public void setDirectory(File dir) {
      if (dir.isDirectory()) {
        this.root = dir;
      }
    }

    public void setGson(Gson gson) {
      this.gson = gson;
    }

    public void setMaxEntries(int maxEntries) {
      this.maxEntries = maxEntries;
    }

    public void setDurationUntilExpiration(long durationUntilExpiration) {
      this.durationUntilExpiration = durationUntilExpiration;
    }

    public RecordBook create() throws FileNotFoundException {
      if (root == null || gson == null) {
        throw new IllegalArgumentException("Cannot reate a recordbook without a root file or " +
                "method of serialization!");
      }

      if (durationUntilExpiration > 0) {
        return new TimedRecordBook(this.root, this.gson, this.durationUntilExpiration);
      } else {
        return new LeaderboardRecordBook(this.root, this.gson, this.maxEntries);
      }

    }

  }

}
