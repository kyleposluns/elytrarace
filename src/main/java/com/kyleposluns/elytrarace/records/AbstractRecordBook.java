package com.kyleposluns.elytrarace.records;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.kyleposluns.elytrarace.util.ERUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

abstract class AbstractRecordBook implements RecordBook {

  protected Map<UUID, List<Record>> recordMap;

  protected final Gson gson;

  protected File root;

  protected AbstractRecordBook(File file, Gson gson) throws FileNotFoundException {
    this.root = file;
    this.gson = gson;
    if (file.exists()) {
      this.recordMap = readFromDisk(file, gson);
    } else {
      this.recordMap = new HashMap<>();
    }
  }

  private static Map<UUID, List<Record>> readFromDisk(File file, Gson gson)
          throws FileNotFoundException {
    Map<UUID, List<Record>> recordMap = new HashMap<>();
    for (File playerData : Objects.requireNonNull(file.listFiles())) {

      if (!ERUtils.getFileExtension(playerData).equals(".json")) continue;

      UUID uniqueId = UUID.fromString(ERUtils.stripFileExtension(playerData.getName()));

      List<Record> records = gson.fromJson(new FileReader(playerData),
              new TypeToken<List<Record>>() {
              }.getType());
      recordMap.put(uniqueId, records);
    }
    return recordMap;
  }

  protected abstract void sanityCheck();

  @Override
  public List<Record> getTopRecords(int n) {
    return getSortedRecords(n, Comparator.comparingLong(Record::getDuration));
  }

  @Override
  public List<Record> getTopRecords(UUID recordHolder, int n) {
    return getSortedRecords(recordHolder, n, Comparator.comparingLong(Record::getDuration));
  }

  @Override
  public List<Record> getRecentRecords(int n) {
    return getSortedRecords(n, Comparator.comparingLong(Record::getFinishTime));
  }

  @Override
  public List<Record> getRecentRecords(UUID recordHolder, int n) {
    return getSortedRecords(recordHolder, n,
            Comparator.comparingLong(Record::getFinishTime));
  }

  @Override
  public double getSuccessRate(UUID recordHolder) {
    List<Record> records = this.recordMap.get(recordHolder);
    int total = records.size();
    if (total == 0) {
      return 0.0;
    }
    long successes = records.stream().filter(Record::finished).count();
    return successes / total;
  }

  @Override
  public double getFailRate(UUID recordHolder) {
    return 1 - getSuccessRate(recordHolder);
  }


  @Override
  public void addRecord(UUID recordHolder, Record record) {
    if (!this.recordMap.containsKey(recordHolder)) {
      this.recordMap.put(recordHolder, new ArrayList<>());
    }

    this.recordMap.get(recordHolder).add(record);

    saveData();
  }

  @Override
  public void saveData() {
    this.sanityCheck();
    for (UUID player : this.recordMap.keySet()) {
      File dataFile = new File(this.root, player.toString() + ".json");
      String json = this.gson.toJson(this.recordMap.get(player));
      try {
        FileOutputStream fos = new FileOutputStream(dataFile);
        fos.write(json.getBytes());
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private List<Record> getSortedRecords(UUID recordHolder, int n,
                                        Comparator<? super Record> comparator) {
    this.sanityCheck();
    return recordMap.get(recordHolder)
            .stream()
            .sorted(comparator)
            .limit(n)
            .collect(Collectors.toList());
  }

  private List<Record> getSortedRecords(int n,
                                        Comparator<? super Record> comparator) {
    this.sanityCheck();
    return recordMap.values()
            .stream()
            .flatMap(Collection::stream)
            .sorted(comparator)
            .limit(n)
            .collect(Collectors.toList());
  }

}