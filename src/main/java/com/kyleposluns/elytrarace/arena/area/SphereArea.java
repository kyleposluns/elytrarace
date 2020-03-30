package com.kyleposluns.elytrarace.arena.area;

import java.util.Objects;
import org.bukkit.util.Vector;

class SphereArea implements Area {

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

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SphereArea)) {
      return false;
    }

    if (o == this) {
      return true;
    }

    SphereArea area = (SphereArea) o;
    return this.center.equals(area.center)
        && this.radius == area.radius;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.center, this.radius);
  }
}
