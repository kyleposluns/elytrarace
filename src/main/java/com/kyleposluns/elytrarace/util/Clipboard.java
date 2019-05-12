package com.kyleposluns.elytrarace.util;

import com.kyleposluns.elytrarace.map.Area;
import org.bukkit.Location;

public class Clipboard {

  private Location pos1;

  private Location pos2;

  public Clipboard() {
    this(null, null);
  }

  public Clipboard(Location pos1, Location pos2) {
    this.pos1 = pos1;
    this.pos2 = pos2;
  }

  public void setPos1(Location pos1) {
    this.pos1 = pos1;
  }

  public void setPos2(Location pos2) {
    this.pos2 = pos2;
  }

  public Location getPos1() {
    return pos1;
  }

  public Location getPos2() {
    return pos2;
  }

  public Area createArea() {
    if (this.pos1 == null || this.pos2 == null) {
      throw new IllegalArgumentException("Cannot create a cuboid from a null clipboard!");
    }

    return new Area(this.pos1, this.pos2);
  }
}
