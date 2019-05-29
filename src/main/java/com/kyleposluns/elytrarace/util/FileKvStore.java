package com.kyleposluns.elytrarace.util;

import com.google.common.reflect.TypeToken;
import com.kyleposluns.elytrarace.ElytraRace;

import java.io.*;
import java.util.*;

public final class FileKvStore<K, V> implements DataAccessManager<K, V> {

  private KeyRepresentation<K> keyRepresentation;

  private final File dir;

  public FileKvStore(File dir, KeyRepresentation<K> keyRepresentation) {
    this.dir = dir;
    this.keyRepresentation = keyRepresentation;

    if (!(dir.exists())) {
      dir.mkdirs();
    }
  }

  @Override
  public Optional<V> get(K key) {
    File jsonFile = getFile(key);
    return this.get(jsonFile);
  }

  @Override
  public Optional<V> get(String key) {
    return this.get(keyRepresentation.convert(key));
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
  public void put(String key, V value) {
    this.put(this.keyRepresentation.convert(key), value);
  }

  @Override
  public void put(Map<K, V> map) {
    for (Map.Entry<K, V> entry : map.entrySet()) {
      this.put(entry.getKey(), entry.getValue());
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
    return new File(this.keyRepresentation.convert(key) + ".json");
  }

  @Override
  public Iterator<Entry<K, V>> entryIter() {
    return new FileKvStoreIterator<>(this);
  }

  private class FileKvStoreIterator<K, V> implements Iterator<Entry<K, V>> {

    private FileKvStore<K, V> storage;

    private int current;

    private List<File> filesInDir;

    FileKvStoreIterator(FileKvStore<K, V> storage) {
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
    public Entry<K, V> next() {
      if (!(hasNext())) {
        throw new IndexOutOfBoundsException("Iterator does not contain a next element!");
      }
      File currentFile = this.filesInDir.get(this.current);
      K key =
              this.storage.keyRepresentation.convert(ERUtils.stripFileExtension(currentFile.getName()));
      Optional<V> valueOptional = this.storage.get(currentFile);
      return valueOptional.map(v -> new Entry<>(key, v)).orElse(null);
    }
  }
}
