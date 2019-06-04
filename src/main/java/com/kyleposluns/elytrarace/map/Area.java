package com.kyleposluns.elytrarace.map;

import org.bukkit.util.Vector;

public class Area {

  private final Vector min;

  private final Vector max;

  public Area(Vector loc1, Vector loc2) {
    this.min = new Vector(
            Math.min(loc1.getX(), loc2.getX()),
            Math.min(loc1.getY(), loc2.getY()),
            Math.min(loc1.getZ(), loc2.getZ()));
    this.max = new Vector(
            Math.max(loc1.getX(), loc2.getX()),
            Math.max(loc1.getY(), loc2.getY()),
            Math.max(loc1.getZ(), loc2.getZ()));
  }

  public boolean isInArea(Vector vector) {
    return vector.isInAABB(this.min, this.max);
  }

  public static boolean isEntering(Area area, Vector to, Vector from) {
    return area.isInArea(to) && !area.isInArea(from);
  }

  public static boolean isExiting(Area area, Vector to, Vector from) {
    return !area.isInArea(to) && area.isInArea(from);
  }

}
