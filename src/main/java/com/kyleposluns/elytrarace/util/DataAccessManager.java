package com.kyleposluns.elytrarace.util;

import java.util.Optional;

public interface DataAccessManager<K, V> extends Iterable<V> {

  Optional<V> get(K key);

  void put(K key, V value);

  void delete(K key);

}
