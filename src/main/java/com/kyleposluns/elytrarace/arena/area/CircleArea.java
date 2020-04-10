package com.kyleposluns.elytrarace.arena.area;

import java.util.Objects;
import org.bukkit.util.Vector;

class CircleArea implements Area {

  private Vector center;

  private double radius;

  private double rotX;

  private double rotY;

  private double rotZ;

  public CircleArea(Vector center, double radius, double rotX, double rotY, double rotZ) {
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

  private Vector rotateOnXAxis(Vector vector, Vector center, double degrees) {
    Vector copy = vector.clone().subtract(center);
    double theta = Math.toRadians(degrees);
    double newX = copy.getX();
    double newY = (copy.getY() * Math.cos(theta)) - (copy.getZ() * Math.sin(theta));
    double newZ = (copy.getZ() * Math.sin(theta)) + (copy.getZ() * Math.cos(theta));

    return new Vector(newX, newY, newZ).add(center);
  }

  private Vector rotateOnZAxis(Vector vector, Vector center, double degrees) {
    Vector copy = vector.clone().subtract(center);
    double theta = Math.toRadians(degrees);

    double newX = (copy.getX() * Math.cos(theta)) - (copy.getY() * Math.sin(theta));
    double newY = (copy.getX() * Math.sin(theta)) + (copy.getY() * Math.cos(theta));
    double newZ = copy.getZ();
    return new Vector(newX, newY, newZ).add(center);
  }

  private Vector rotateOnYAxis(Vector vector, Vector center, double degrees) {
    Vector copy = vector.clone().subtract(center);
    double theta = Math.toRadians(degrees);
    double newX = (copy.getX() * Math.cos(theta)) + (copy.getZ() * Math.sin(theta));
    double newY = copy.getY();
    double newZ = (copy.getX() * -1 * Math.sin(theta)) + (copy.getZ() * Math.cos(theta));
    return new Vector(newX, newY, newZ).add(center);
  }

  private Vector rotate(Vector vector, Vector center) {
    return rotateOnXAxis(
        rotateOnYAxis(rotateOnZAxis(vector, center, -this.getRotZ()), center, -this.getRotY()),
        center, -this.getRotX());
  }

  @Override
  public boolean contains(Vector vector) {
    Vector rotatedCenter = rotate(this.center, this.center);

    //System.out.println("Rotated center: " + rotatedCenter.toString());

    Vector rotatedPoint = rotate(vector, this.center);

    //System.out.println("Rotated point: " + rotatedPoint.toString());

    double distanceSquared =
        ((rotatedCenter.getY() - rotatedPoint.getY()) * (rotatedCenter.getY() - rotatedPoint
            .getY()))
            + ((rotatedCenter.getZ() - rotatedPoint.getZ()) * (rotatedCenter.getZ() - rotatedPoint
            .getZ()));

    double distance = Math.sqrt(distanceSquared);


    return distance <= this.radius && Math.abs(rotatedCenter.getX() - rotatedPoint.getX()) <= 0.5;
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
}
