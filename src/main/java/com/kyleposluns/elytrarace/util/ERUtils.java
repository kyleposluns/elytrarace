package com.kyleposluns.elytrarace.util;

import com.kyleposluns.elytrarace.records.Record;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ERUtils {

  public static String stripFileExtension(String fileName) {
    int pos = fileName.lastIndexOf(".");
    if (pos > 0) {
      fileName = fileName.substring(0, pos);
    }
    return fileName;
  }

  public static String getFileExtension(File file) {
    String name = file.getName();
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // empty extension
    }
    return name.substring(lastIndexOf);
  }


  public static <T> boolean insert(LinkedList<T> top, T t, Comparator<T> tComparator, int maxTop) {
    ListIterator<T> topIterator = top.listIterator();
    while(topIterator.hasNext()) {
      T at = topIterator.next();
      if (tComparator.compare(t, at) < 0) {
        topIterator.previous();
        topIterator.add(t);
        if (top.size() > maxTop) {
          top.removeLast();
        }
        return true;
      }
    }
    return false;
  }

}
