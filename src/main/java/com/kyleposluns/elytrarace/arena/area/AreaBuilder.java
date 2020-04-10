package com.kyleposluns.elytrarace.arena.area;

import org.bukkit.Axis;
import org.bukkit.util.Vector;

public class AreaBuilder {

  private double radius;

  private double rotX;

  private double rotY;

  private double rotZ;

  private Axis axis;

  private Vector center;

  private Vector position1;

  private Vector position2;


  public AreaBuilder() {
    this.radius = 0.0;
    this.axis = null;
    this.center = null;
    this.rotX = 0.0;
    this.rotY = 0.0;
    this.rotZ = 0.0;
    this.position1 = null;
    this.position2 = null;
  }

  public AreaBuilder radius(double radius) {
    this.radius = radius;
    return this;
  }

  public AreaBuilder axis(Axis axis) {
    this.axis = axis;
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

  public Area build() {
    if (this.radius != 0) {

      if (this.center == null) {
        throw new IllegalStateException(
            "Could not build an area with the provided information");
      }
      if (this.axis == null) {
        return new SphereArea(this.center, this.radius);
      } else {
        return new CircleArea(this.center, 0, this.rotX, this.rotY, this.rotZ);
      }

    } else {
      if (this.position1 != null && this.position2 != null) {
        return new CuboidArea(this.position1, this.position2);
      } else {
        throw new IllegalStateException(
            "Could not build an area with the provided information.");
      }

    }
  }

}