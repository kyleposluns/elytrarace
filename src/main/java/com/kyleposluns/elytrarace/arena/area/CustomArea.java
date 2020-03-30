package com.kyleposluns.elytrarace.arena.area;

import java.util.List;
import org.bukkit.util.Vector;

class CustomArea implements Area {

  private List<Vector> vectors;

  public CustomArea(List<Vector> vectors) {
    this.vectors = vectors;
  }

  public List<Vector> getVectors() {
    return this.vectors;
  }

  @Override
  public boolean contains(Vector vector) {
    return vectors.stream().anyMatch(v ->
        v.getBlockX() == vector.getBlockX() && v.getBlockY() ==
            vector.getBlockY() && v.getBlockZ() == vector.getBlockZ());
  }

  @Override
  public <R> R visitArea(AreaVisitor<R> visitor) {
    return visitor.visitCustom(this);
  }

}
