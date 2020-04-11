package com.kyleposluns.elytrarace.arena.area;

import org.bukkit.util.Vector;

public class AreaBuilder {

  private AreaType type;

  private double radius;

  private Double rotX;

  private Double rotY;

  private Double rotZ;

  private Vector center;

  private Vector position1;

  private Vector position2;


  public AreaBuilder() {
    this.radius = 0.0;
    this.type = null;
    this.center = null;
    this.rotX = null;
    this.rotY = null;
    this.rotZ = null;
    this.position1 = null;
    this.position2 = null;
  }

  public AreaBuilder radius(double radius) {
    this.radius = radius;
    return this;
  }

  public AreaBuilder center(Vector center) {
    this.center = center;
    return this;
  }

  public AreaBuilder rotationX(double rotX) {
    this.rotX = rotX;
    return this;
  }

  public AreaBuilder rotationY(double rotY) {
    this.rotY = rotY;
    return this;
  }

  public AreaBuilder rotationZ(double rotZ) {
    this.rotZ = rotZ;
    return this;
  }

  public AreaBuilder position1(Vector position) {
    this.position1 = position;
    return this;
  }

  public AreaBuilder position2(Vector position) {
    this.position2 = position;
    return this;
  }

  public AreaType type(AreaType type) {
    this.type = type;
    return null;
  }

  public Area build() {
    if (this.type == null) {
      throw new IllegalStateException(
          "Could not build an area with the provided information");
    }

    if (this.radius != 0) {

      if (this.center == null) {
        throw new IllegalStateException(
            "Could not build an area with the provided information");
      }

      if (this.rotX == null || this.rotY == null || this.rotZ == null) {
        return new SphereArea(this.type, this.center, this.radius);
      } else {
        return new CircleArea(this.type, this.center, this.radius, this.rotX, this.rotY, this.rotZ);
      }

    } else {
      if (this.position1 != null && this.position2 != null) {
        return new CuboidArea(this.type, this.position1, this.position2);
      } else {
        throw new IllegalStateException(
            "Could not build an area with the provided information.");
      }

    }
  }

}