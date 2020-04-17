package com.kyleposluns.elytrarace.records;

import com.kyleposluns.elytrarace.MessageFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecordBookPrinter implements Function<RecordBook, List<String>> {

  private final MessageFormatter formatter;

  public RecordBookPrinter(MessageFormatter formatter) {
    this.formatter = formatter;
  }

  @Override
  public List<String> apply(RecordBook records) {
    return records.getTopRecords(10)
        .stream().map(record -> new RecordPrinter(this.formatter).apply(record))
        .collect(Collectors.toList());
  }
}
