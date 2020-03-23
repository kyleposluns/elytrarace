package com.kyleposluns.elytrarace.map;

import org.bukkit.Axis;
import org.bukkit.util.Vector;

public class CircleArea implements Area {

  private final Vector center;

  private final Axis axis;

  private final double radius;

  public CircleArea(Vector center, Axis axis, double radius) {
    this.center = center;
    this.axis = axis;
    this.radius = radius;
  }

  public Vector getCenter() {
    return this.center;
  }

  public Axis getAxis() {
    return this.axis;
  }

  public double getRadius() {
    return this.radius;
  }

  @Override
  public boolean contains(Vector vector) {
    switch (this.axis) {
      case X: {
        return Math.abs(this.center.getBlockZ() - vector.getBlockZ()) < this.radius
            && Math.abs(this.center.getBlockY() - vector.getBlockY()) < this.radius
            && this.center.getBlockX() == vector.getBlockX();
      }
      case Y: {
        return Math.abs(this.center.getBlockZ() - vector.getBlockZ()) < this.radius
            && Math.abs(this.center.getBlockX() - vector.getBlockX()) < this.radius
            && this.center.getBlockY() == vector.getBlockY();
      }
      case Z: {
        return Math.abs(this.center.getBlockY() - vector.getBlockY()) < this.radius
            && Math.abs(this.center.getBlockX() - vector.getBlockX()) < this.radius
            && this.center.getBlockZ() == vector.getBlockZ();
      }
      default: {
        throw new IllegalArgumentException("Cannot interpret an unknown axis.");
      }
    }
  }

  @Override
  public <R> R visitArea(AreaVisitor<R> visitor) {
    return visitor.visitCircle(this);
  }
}
