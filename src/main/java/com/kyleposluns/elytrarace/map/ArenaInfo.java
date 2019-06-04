package com.kyleposluns.elytrarace.map;


import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaInfo {

  private final String name;

  private final String creator;

  private final UUID worldId;

  private final UUID arenaId;

  private final List<Area> borders;

  private final Area goal;

  private final Area start;

  private final Vector spawn;

  private final float spawnYaw;

  private final float spawnPitch;


  private ArenaInfo(String name, String creator, UUID arenaId, UUID worldId, List<Area> borders,
                    Area goal,
                    Area start,
                    Vector spawn, float spawnYaw, float spawnPitch) {
    this.name = name;
    this.creator = creator;
    this.arenaId = arenaId;
    this.worldId = worldId;
    this.borders = borders;
    this.goal = goal;
    this.start = start;
    this.spawn = spawn;
    this.spawnYaw = spawnYaw;
    this.spawnPitch = spawnPitch;
  }

  public UUID getArenaId() {
    return this.arenaId;
  }

  public UUID getWorldId() {
    return this.worldId;
  }

  public String getName() {
    return this.name;
  }

  public String getCreator() {
    return this.creator;
  }

  public List<Area> getBorders() {
    return this.borders;
  }

  public Area getStart() {
    return this.start;
  }

  public Vector getSpawn() {
    return this.spawn;
  }

  public Area getGoal() {
    return this.goal;
  }

  public float getSpawnYaw() {
    return this.spawnYaw;
  }

  public float getSpawnPitch() {
    return this.spawnPitch;
  }

  public static class Builder {

    private String name;

    private String creator;

    private UUID arenaId;

    private UUID worldId;

    private List<Area> borders;

    private Area goal;

    private Area start;

    private Vector spawn;

    private float spawnYaw;

    private float spawnPitch;

    public Builder() {
      this.arenaId = UUID.randomUUID();
      this.borders = new ArrayList<>();
    }

    public void spawn(Location spawn) {
      this.spawn = new Vector(spawn.getX(), spawn.getY(), spawn.getZ());
      this.spawnYaw = spawn.getYaw();
      this.spawnPitch = spawn.getPitch();
    }

    public void spawn(Vector spawn) {
      this.spawn = spawn;
      this.spawnYaw = 0;
      this.spawnPitch = 0;
    }

    public void goal(Area goal) {
      this.goal = goal;
    }

    public void start(Area start) {
      this.start = start;
    }

    public void borders(List<Area> borders) {
      this.borders = borders;
    }

    public void border(Area area) {
      this.borders.add(area);
    }

    public void creator(String creator) {
      this.creator = creator;
    }

    public void name(String name) {
      this.name = name;
    }

    public void world(UUID worldId) {
      this.worldId = worldId;
    }

    public ArenaInfo create() {
      if (this.name == null || this.creator == null || this.arenaId == null
              || this.worldId == null || this.borders == null
              || this.goal == null || this.start == null || this.spawn == null) {
        throw new IllegalArgumentException("Cannot create an ArenaInfo from null data.");
      }
      return new ArenaInfo(this.name, this.creator, this.arenaId, this.worldId, this.borders,
              this.goal,
              this.start,
              this.spawn, this.spawnYaw, this.spawnPitch);
    }

  }


}
