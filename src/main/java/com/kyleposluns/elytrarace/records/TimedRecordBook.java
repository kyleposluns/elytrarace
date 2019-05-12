package com.kyleposluns.elytrarace.records;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class TimedRecordBook extends AbstractRecordBook {

  private final long durationUntilExpiration;

  TimedRecordBook(File file, Gson gson, long durationUntilExpiration) throws FileNotFoundException {
    super(file, gson);
    this.durationUntilExpiration = durationUntilExpiration;
  }

  private void keepMostRecent() {
    this.recordMap = this.recordMap.entrySet()
            .stream()
            .map(uuidListEntry -> new AbstractMap.SimpleEntry<>(uuidListEntry.getKey(),
                    uuidListEntry.getValue()
                            .stream()
                            .filter(record -> System.currentTimeMillis() - record.getFinishTime()
                                    < TimeUnit.SECONDS.toMillis(this.durationUntilExpiration))
                            .collect(Collectors.toList())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  protected void sanityCheck() {
    this.keepMostRecent();
  }
}
