package com.kyleposluns.elytrarace.map;

import org.bukkit.util.Vector;

public class SphereArea implements Area {

  private final Vector center;

  private final double radius;

  public SphereArea(Vector center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  public Vector getCenter() {
    return this.center;
  }

  public double getRadius() {
    return this.radius;
  }

  @Override
  public boolean contains(Vector vector) {
    return vector.isInSphere(this.center, this.radius);
  }

  @Override
  public <R> R visitArea(AreaVisitor<R> visitor) {
    return visitor.visitSphere(this);
  }
}
