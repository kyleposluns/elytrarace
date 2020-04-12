package com.kyleposluns.elytrarace.arena.area;

import java.util.Objects;
import org.bukkit.util.Vector;

class CircleArea extends AbstractArea {

  private Vector center;

  private double radius;

  private double rotX;

  private double rotY;

  private double rotZ;

  public CircleArea(AreaType type, int order, Vector center, double radius, double rotX, double rotY, double rotZ) {
    super(type, order);
    this.center = center;
    this.radius = radius;
    this.rotX = rotX;
    this.rotY = rotY;
    this.rotZ = rotZ;
  }

  public Vector getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
  }

  public double getRotX() {
    return this.rotX;
  }

  public double getRotY() {
    return this.rotY;
  }

  public double getRotZ() {
    return this.rotZ;
  }

  @Override
  public boolean contains(Vector vector) {
    Vector rotatedCenter = rotateZYX(this.center, this.center, -1 * this.rotX, -1 * this.rotY,
        -1 * this.rotZ);

    Vector rotatedPoint = rotateZYX(vector, this.center, -1 * this.rotX, -1 * this.rotY,
        -1 * this.rotZ);

    double distanceSquared =
        ((rotatedCenter.getY() - rotatedPoint.getY()) * (rotatedCenter.getY() - rotatedPoint
            .getY()))
            + ((rotatedCenter.getZ() - rotatedPoint.getZ()) * (rotatedCenter.getZ() - rotatedPoint
            .getZ()));

    double distance = Math.sqrt(distanceSquared);

    return distance <= this.radius && Math.abs(rotatedCenter.getX() - rotatedPoint.getX()) <= 1.5;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CircleArea)) {
      return false;
    }

    if (o == this) {
      return true;
    }

    CircleArea area = (CircleArea) o;
    return this.rotX == area.rotX
        && this.rotY == area.rotY
        && this.rotZ == area.rotZ
        && this.center.equals(area.center)
        && this.radius == area.radius;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.center, this.rotX, this.rotY, this.rotZ, this.radius);
  }

  @Override
  public <R> R visitArea(AreaVisitor<R> visitor) {
    return visitor.visitCircle(this);
  }


  static Vector rotateZYX(Vector point, Vector center, double rotX, double rotY,
      double rotZ) {
    double theta = Math.toRadians(rotX);
    double phi = Math.toRadians(rotY);
    double psi = Math.toRadians(rotZ);
    Vector v = point.clone().subtract(center);

    double x1 = v.getX() * (Math.cos(phi) * Math.cos(psi));
    double x2 = v.getY() * (-1 * Math.cos(phi) * Math.sin(psi));
    double x3 = v.getZ() * (Math.sin(phi));

    double newX = x1 + x2 + x3;

    double y1 =
        v.getX() * ((Math.sin(theta) * Math.sin(phi) * Math.cos(psi)) + (Math.cos(theta) * Math
            .sin(psi)));
    double y2 =
        v.getY() * ((-1 * Math.sin(theta) * Math.sin(phi) * Math.sin(psi)) + (Math.cos(theta) * Math
            .cos(theta)));
    double y3 = v.getZ() * (-1 * Math.sin(theta) * Math.cos(phi));

    double newY = y1 + y2 + y3;

    double z1 =
        v.getX() * ((-1 * Math.cos(theta) * Math.sin(phi) * Math.cos(psi)) + (Math.sin(theta) * Math
            .sin(psi)));
    double z2 =
        v.getY() * ((Math.cos(theta) * Math.sin(phi) * Math.sin(psi)) + (Math.sin(theta) * Math
            .cos(psi)));
    double z3 = v.getZ() * (Math.cos(theta) * Math.cos(phi));

    double newZ = z1 + z2 + z3;

    return new Vector(newX, newY, newZ).add(center);
  }


  static Vector rotateXYZ(Vector point, Vector center, double rotX, double rotY,
      double rotZ) {
    double theta = Math.toRadians(rotX);
    double phi = Math.toRadians(rotY);
    double psi = Math.toRadians(rotZ);
    Vector v = point.clone().subtract(center);

    double x1 = v.getX() * (Math.cos(psi) * Math.cos(phi));
    double x2 =
        v.getY() * ((-1 * Math.sin(psi) * Math.cos(theta)) + (Math.cos(psi) * Math.sin(phi) * Math
            .sin(theta)));
    double x3 =
        v.getZ() * ((Math.sin(psi) * Math.sin(theta)) + (Math.cos(psi) * Math.sin(phi) * Math
            .cos(theta)));
    double newX = x1 + x2 + x3;

    double y1 = (v.getX() * (Math.sin(psi) * Math.cos(phi)));
    double y2 = (v.getY() * ((Math.cos(psi) * Math.cos(theta)) + (Math.sin(psi) * Math.sin(phi)
        * Math
        .sin(theta))));
    double y3 = (v.getZ() * ((-1 * Math.cos(psi) * Math.sin(theta)) + (Math.sin(psi) * Math.sin(phi)
        * Math
        .cos(theta))));

    double newY = y1 + y2 + y3;

    double z1 = (v.getX() * (-1 * Math.sin(phi)));
    double z2 = (v.getY() * (Math.cos(phi) * Math.sin(theta)));
    double z3 = (v.getZ() * (Math.cos(phi) * Math.cos(theta)));

    double newZ = z1 + z2 + z3;

    return new Vector(newX, newY, newZ).add(center);
  }
}
