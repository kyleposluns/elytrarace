package com.kyleposluns.elytrarace.util;

import java.util.function.Function;

public class KeyRepresentation<T> {

  private Function<T, String> toString;

  private Function<String, T> fromString;

  public KeyRepresentation(Function<T, String> toString, Function<String, T> fromString) {
    this.toString = toString;
    this.fromString = fromString;
  }

  public String convert(T key) {
    return this.toString.apply(key);
  }

  public T convert(String key) {
    return this.fromString.apply(key);
  }

}
