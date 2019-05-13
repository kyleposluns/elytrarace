package com.kyleposluns.elytrarace.map;


import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class ArenaInfo {

  private final String name;

  private final String creator;

  private final Area[] borders;

  private final Area goal;

  private final Area start;

  private final Location spawn;


  private ArenaInfo(String name, String creator, Area[] borders, Area goal, Area start,
                    Location spawn) {
    this.name = name;
    this.creator = creator;
    this.borders = borders;
    this.goal = goal;
    this.start = start;
    this.spawn = spawn;
  }

  public String getName() {
    return this.name;
  }

  public String getCreator() {
    return this.creator;
  }

  public Area[] getBorders() {
    return this.borders;
  }

  public Area getStart() {
    return this.start;
  }

  public Location getSpawn() {
    return this.spawn;
  }

  public Area getGoal() {
    return this.goal;
  }

  public static class Builder {

    private String name;

    private String creator;

    private Area[] borders;

    private Area goal;

    private Area start;

    private Location spawn;


    public Builder() {
      this.borders = new Area[0];
    }


    public void spawn(Location spawn) {
      this.spawn = spawn;
    }

    public void goal(Area goal) {
      this.goal = goal;
    }

    public void start(Area start) {
      this.start = start;
    }

    public void borders(Area[] borders) {
      this.borders = borders;
    }

    public void border(Area border) {
      List<Area> areas = Arrays.asList(this.borders);
      areas.add(border);
      this.borders = areas.toArray(new Area[]{});
    }

    public void creator(String creator) {
      this.creator = creator;
    }

    public void name(String name) {
      this.name = name;
    }

    public ArenaInfo create() {
      if (this.name == null || this.creator == null || this.borders == null
              || this.goal == null || this.start == null || this.spawn == null) {
        throw new IllegalArgumentException("Cannot create an ArenaInfo from null data.");
      }
      return new ArenaInfo(this.name, this.creator, this.borders, this.goal, this.start,
              this.spawn);
    }


  }



}
