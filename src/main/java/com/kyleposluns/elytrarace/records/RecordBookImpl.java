package com.kyleposluns.elytrarace.records;

import com.kyleposluns.elytrarace.ElytraRace;
import com.kyleposluns.elytrarace.util.DataAccessManager;
import com.kyleposluns.elytrarace.util.FileKvStore;
import com.kyleposluns.elytrarace.util.KeyRepresentation;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

public final class RecordBookImpl implements RecordBook {

  private static final Comparator<Record> COMPARE_BY_TIME =
          Comparator.comparingLong(o -> o.getRaw().getDuration());

  private static final KeyRepresentation<UUID> KEY_REPRESENTATION =
          new KeyRepresentation<>(UUID::toString,
                  UUID::fromString);

  private int autoSaveTask;

  private final int maxTop;

  private final Comparator<Record> recordComparator;

  private final LinkedList<Record> topRecords;

  private final DataAccessManager<UUID, PlayerDataEntry> database;

  public RecordBookImpl(File arenaFile) {
    this.maxTop = ElytraRace.getInstance().getGameConfig().getMaxEntries();
    this.recordComparator = COMPARE_BY_TIME;
    this.database = new FileKvStore<>(arenaFile, KEY_REPRESENTATION);
    this.topRecords = this.parseTopRecords();
  }

  private LinkedList<Record> parseTopRecords() {

    LinkedList<Record> top = new LinkedList<>();

    Iterator<DataAccessManager.Entry<UUID, PlayerDataEntry>> entryIterator =
            this.database.entryIter();

    while (entryIterator.hasNext()) {
      DataAccessManager.Entry<UUID, PlayerDataEntry> entry = entryIterator.next();
      for (RecordEntry r : entry.getValue().getRecords()) {
        insert(top, new Record(entry.getKey(), r), this.recordComparator, this.maxTop);
      }
    }
    return top;
  }

  @Override
  public List<Vector> getFastestPath() {
    if (this.topRecords.isEmpty()) {
      return new ArrayList<>();
    }
    return this.topRecords.getFirst().getRaw().getPath();
  }

  @Override
  public Optional<PlayerDataEntry> getPlayerEntry(UUID owner) {
    return this.database.get(owner);
  }

  @Override
  public List<Record> getTopRecords() {
    return this.topRecords;
  }

  @Override
  public void addRecord(Record record) {

    insert(this.topRecords, record, this.recordComparator, this.maxTop);
    UUID uniqueId = record.getUniqueId();
    Optional<PlayerDataEntry> entryOptional = this.getPlayerEntry(uniqueId);

    PlayerDataEntry entry = entryOptional.orElse(new PlayerDataEntry());

    entry.getRecords().add(record.getRaw());

    this.database.put(uniqueId, entry);

  }

  private static <T> boolean insert(LinkedList<T> list, T t, Comparator<T> tComparator,
                                   int maxLen) {
    ListIterator<T> topIterator = list.listIterator();
    while (topIterator.hasNext()) {
      T at = topIterator.next();
      if (tComparator.compare(t, at) < 0) {
        topIterator.previous();
        topIterator.add(t);
        if (list.size() > maxLen) {
          list.removeLast();
        }
        return true;
      }
    }
    return false;
  }
}
