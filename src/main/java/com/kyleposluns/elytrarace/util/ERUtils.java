package com.kyleposluns.elytrarace.util;

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


}
