package com.kyleposluns.elytrarace.util;

import com.google.common.reflect.TypeToken;
import com.kyleposluns.elytrarace.ElytraRace;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
      if (!jsonFile.exists()) {
        jsonFile.createNewFile();
      }
      FileOutputStream fos = new FileOutputStream(jsonFile);
      fos.write(json.getBytes());
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void putMultiple(Map<K, V> map) {
    for (Map.Entry<K, V> entry : map.entrySet()) {
      this.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public boolean delete(K key) {
    File jsonFile = getFile(key);

    if (jsonFile.exists()) {
      return jsonFile.delete();
    }
    return false;
  }

  private File getFile(K key) {
    return new File(this.keyRepresentation.convert(key) + ".json");
  }

  @Override
  public Iterator<Entry<K, V>> entryIter() {
    return new FileKvStoreIterator<>(this);
  }

  private class FileKvStoreIterator<K, V> implements Iterator<Entry<K, V>> {

    private FileKvStore<K, V> parent;

    private int current;

    private List<K> keys;

    FileKvStoreIterator(FileKvStore<K, V> parent) {
      this.parent = parent;
      if (this.parent.dir.isFile()) {
        throw new IllegalArgumentException("Can only iterate through " +
                "directories!");
      }
      this.keys = Arrays.stream(Objects.requireNonNull(this.parent.dir.listFiles()))
              .map(file -> parent.keyRepresentation.convert(ERUtils.stripFileExtension(file.getName())))
              .collect(Collectors.toList());
      this.current = 0;
    }

    @Override
    public boolean hasNext() {
      return this.current < this.keys.size();
    }

    @Override
    public Entry<K, V> next() {
      if (!(hasNext())) {
        throw new IndexOutOfBoundsException("Iterator does not contain a next element!");
      }

      K key = this.keys.get(this.current);
      Optional<V> valueOptional = this.parent.get(key);
      return valueOptional.map(v -> new Entry<>(key, v)).orElse(null);
    }
  }
}
