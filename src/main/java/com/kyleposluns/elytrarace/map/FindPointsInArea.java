package com.kyleposluns.elytrarace.map;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.Vector;

public class FindPointsInArea implements AreaVisitor<List<Vector>> {

  @Override
  public List<Vector> visitCuboid(CuboidArea cuboid) {
    List<Vector> vectors = new ArrayList<>();
    Vector min = cuboid.getMin();
    Vector max = cuboid.getMax();
    for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
      for (int y = min.getBlockY(); y < max.getBlockY(); y++) {
        for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
          vectors.add(new Vector(x, y, z));
        }
      }
    }
    return vectors;
  }

  @Override
  public List<Vector> visitSphere(SphereArea sphere) {
    List<Vector> vectors = new ArrayList<>();
    int centerX = sphere.getCenter().getBlockX();
    int centerY = sphere.getCenter().getBlockY();
    int centerZ = sphere.getCenter().getBlockZ();
    int radius = (int) Math.ceil(sphere.getRadius());
    for (int x = centerX - radius; x < centerX + radius; x++) {
      for (int y = centerY - radius; y < centerY + radius; y++) {
        for (int z = centerZ - radius; z < centerZ + radius; z++) {
          vectors.add(new Vector(x, y, z));
        }
      }
    }
    return vectors;
  }

  @Override
  public List<Vector> visitCustom(CustomArea custom) {
    return custom.getVectors();
  }

  @Override
  public List<Vector> visitCircle(CircleArea circle) {
    return null;
  }
}
