package com.kyleposluns.elytrarace.util;

import com.google.common.reflect.TypeToken;
import com.kyleposluns.elytrarace.ElytraRace;

import java.io.*;
import java.util.*;

public final class FileKvStore<K, V> implements DataAccessManager<K, V> {

  private final File dir;

  public FileKvStore(File dir) {
    this.dir = dir;

    if (!(dir.exists())) {
      dir.mkdirs();
    }
  }

  @Override
  public Optional<V> get(K key) {
    File jsonFile = getFile(key);
    return get(jsonFile);
  }

  private Optional<V> get(File file) {
    try {
      return Optional.ofNullable(ElytraRace.getGson().fromJson(new FileReader(file),
              new TypeToken<V>() {
              }.getType()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public void put(K key, V value) {
    File jsonFile = getFile(key);
    String json = ElytraRace.getGson().toJson(value);
    try {
      FileOutputStream fos = new FileOutputStream(jsonFile);
      fos.write(json.getBytes());
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(K key) {
    File jsonFile = getFile(key);

    if (jsonFile.exists()) {
      jsonFile.delete();
    }

  }

  private File getFile(K key) {
    return new File(key.toString() + ".json");
  }

  @Override
  public Iterator<V> iterator() {
    return new FileKvStoreIterator<>(this);
  }

  private class FileKvStoreIterator<V> implements Iterator<V> {

    private FileKvStore<?, V> storage;

    private int current;

    private List<File> filesInDir;

    FileKvStoreIterator(FileKvStore<?, V> storage) {
      this.storage = storage;
      if (this.storage.dir.isFile()) {
        throw new IllegalArgumentException("Can only iterate through " +
                "directories!");
      }
      this.filesInDir = Arrays.asList(Objects.requireNonNull(this.storage.dir.listFiles()));
      this.current = 0;
    }

    @Override
    public boolean hasNext() {
      return this.current < this.filesInDir.size();
    }

    @Override
    public V next() {
      if (!(hasNext())) {
        throw new IndexOutOfBoundsException("Iterator does not contain a next element!");
      }
      File currentFile = this.filesInDir.get(this.current);
      Optional<V> obj = this.storage.get(currentFile);
      this.current = this.current + 1;
      return obj.orElse(null);
    }
  }
}
