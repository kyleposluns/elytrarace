package com.kyleposluns.elytrarace.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public interface DataAccessManager<K, V> {

  Optional<V> get(K key);

  void put(K key, V value);

  void putMultiple(Map<K, V> map);

  boolean delete(K key);

  Iterator<Entry<K, V>> entryIter();

  class Entry<K, V> {

    private K key;

    private V value;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return this.key;
    }

    public V getValue() {
      return this.value;
    }


  }

}
