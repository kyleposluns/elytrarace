package com.kyleposluns.elytrarace.arena.area;


import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.Vector;

public class DrawOutline implements AreaVisitor<List<Vector>> {

  private double granularity;

  public DrawOutline(double granularity) {
    this.granularity = granularity;
  }

  public DrawOutline() {
    this.granularity = -1;
  }

  @Override
  public List<Vector> visitCuboid(CuboidArea cuboid) {
    List<Vector> vectors = new ArrayList<>();
    double delta = this.granularity < 0 ? 1 : this.granularity;
    Vector min = cuboid.getMin();
    Vector max = cuboid.getMax();

    for (double x = min.getX(); x < max.getX(); x = x + delta) {
      vectors.add(new Vector(x, min.getY(), min.getZ()));
      vectors.add(new Vector(x, max.getY(), max.getZ()));
      vectors.add(new Vector(x, min.getY(), max.getZ()));
      vectors.add(new Vector(x, max.getY(), min.getZ()));
    }

    for (double y = min.getY(); y < max.getY(); y = y + delta) {
      vectors.add(new Vector(min.getX(), y, min.getZ()));
      vectors.add(new Vector(max.getX(), y, max.getZ()));
      vectors.add(new Vector(min.getX(), y, max.getZ()));
      vectors.add(new Vector(max.getX(), y, min.getZ()));
    }

    for (double z = min.getZ(); z < max.getZ(); z = z + delta) {
      vectors.add(new Vector(min.getX(), min.getY(), z));
      vectors.add(new Vector(max.getX(), max.getY(), z));
      vectors.add(new Vector(max.getX(), min.getY(), z));
      vectors.add(new Vector(min.getX(), max.getY(), z));
    }

    return vectors;
  }

  @Override
  public List<Vector> visitSphere(SphereArea sphere) {
    double deltaTheta = this.granularity < 0 ? Math.PI / 10 : this.granularity;
    List<Vector> vectors = new ArrayList<>();

    for (double i = 0; i <= Math.PI; i += deltaTheta) {
      double radius = sphere.getRadius() * Math.sin(i);
      double y = Math.cos(i);
      for (double a = 0; a < Math.PI * 2; a += deltaTheta) {
        double x = Math.cos(a) * radius;
        double z = Math.sin(a) * radius;
        Vector point = sphere.getCenter().clone().add(new Vector(x, y, z));
        vectors.add(point);
      }
    }
    return vectors;
  }

  @Override
  public List<Vector> visitCircle(CircleArea circle) {
    double deltaTheta = this.granularity < 0 ? Math.PI / 24 : this.granularity;
    List<Vector> vectors = new ArrayList<>();

    for (double a = 0; a < Math.PI * 2; a += deltaTheta) {
      double x = 0;
      double y = Math.cos(a) * circle.getRadius();
      double z = Math.sin(a) * circle.getRadius();
      Vector point = circle.getCenter().clone().add(new Vector(x, y, z));
      Vector rotatedPoint = CircleArea
          .rotateXYZ(point, circle.getCenter(), circle.getRotX(), circle.getRotY(),
              circle.getRotZ());
      vectors.add(rotatedPoint);
    }
    return vectors;
  }
}
