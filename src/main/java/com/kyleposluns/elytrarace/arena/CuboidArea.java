package com.kyleposluns.elytrarace.arena;

import org.bukkit.util.Vector;

public class CuboidArea implements Area {

  private final Vector min;

  private final Vector max;

  public CuboidArea(Vector pos1, Vector pos2) {
    this.min = Vector.getMinimum(pos1, pos2);
    this.max = Vector.getMaximum(pos1, pos2);
  }

  public Vector getMin() {
    return this.min;
  }

  public Vector getMax() {
    return this.max;
  }

  @Override
  public boolean contains(Vector vector) {
    return vector.isInAABB(this.min, this.max);
  }

  @Override
  public <R> R visitArea(AreaVisitor<R> visitor) {
    return visitor.visitCuboid(this);
  }
}
