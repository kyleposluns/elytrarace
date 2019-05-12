package com.kyleposluns.elytrarace.map;

import org.bukkit.Location;

public class Area {

  private final Location min;

  private final Location max;

  public Area(Location loc1, Location loc2) {
    if (!loc1.getWorld().equals(loc2.getWorld())) {
      throw new IllegalArgumentException("Cannot create an area across multiple worlds!");
    }

    this.min = new Location(loc1.getWorld(),
            Math.min(loc1.getX(), loc2.getX()),
            Math.min(loc1.getY(), loc2.getY()),
            Math.min(loc1.getZ(), loc2.getZ()));
    this.max = new Location(loc1.getWorld(),
            Math.max(loc1.getX(), loc2.getX()),
            Math.max(loc1.getY(), loc2.getY()),
            Math.max(loc1.getZ(), loc2.getZ()));

  }


  public boolean isInArea(Location location) {
    return (location.getX() < max.getX() && location.getX() > min.getX())
            && (location.getY() < max.getY() && location.getY() > min.getY())
            && (location.getZ() < max.getZ() && location.getZ() > min.getZ());

  }


}
