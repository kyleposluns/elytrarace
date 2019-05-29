package com.kyleposluns.elytrarace.records;

import com.kyleposluns.elytrarace.util.DataAccessManager;
import com.kyleposluns.elytrarace.util.ERUtils;
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

  private static final int DEFAULT_MAX_TOP = 5;

  private final int maxTop;

  private final Comparator<Record> recordComparator;

  private final LinkedList<Record> topRecords;

  private final DataAccessManager<UUID, PlayerDataEntry> database;

  private final Map<UUID, PlayerDataEntry> playerCache;

  RecordBookImpl(File arenaFile, Comparator<Record> recordComparator, int maxTop) {
    this.maxTop = maxTop;
    this.recordComparator = recordComparator;
    this.database = new FileKvStore<>(arenaFile, KEY_REPRESENTATION);
    this.topRecords = this.parseTopRecords();
    this.playerCache = new HashMap<>();
  }

  public RecordBookImpl(File arenaFile) {
    this(arenaFile, COMPARE_BY_TIME, DEFAULT_MAX_TOP);
  }

  private LinkedList<Record> parseTopRecords() {

    LinkedList<Record> top = new LinkedList<>();

    Iterator<DataAccessManager.Entry<UUID, PlayerDataEntry>> entryIterator =
            this.database.entryIter();

    while (entryIterator.hasNext()) {
      DataAccessManager.Entry<UUID, PlayerDataEntry> entry = entryIterator.next();
      this.playerCache.put(entry.getKey(), entry.getValue());
      for (RecordEntry r : entry.getValue().getRecords()) {
        ERUtils.insert(top, new Record(entry.getKey(), r), this.recordComparator, this.maxTop);
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
    return Optional.ofNullable(playerCache.get(owner));
  }

  @Override
  public List<Record> getTopRecords() {
    return this.topRecords;
  }

  @Override
  public void save() {
    this.database.put(this.playerCache);
  }

  @Override
  public void addPlayer(UUID playerId) {
    this.playerCache.put(playerId, new PlayerDataEntry());
  }

  @Override
  public void addRecord(Record record) {
    ERUtils.insert(this.topRecords, record, this.recordComparator, this.maxTop);
    this.getPlayerEntry(record.getRecordId())
            .ifPresent(playerData -> playerData.addEntry(record.getRaw()));
  }
}
