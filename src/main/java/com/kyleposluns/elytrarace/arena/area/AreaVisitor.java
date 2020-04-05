package com.kyleposluns.elytrarace.arena.area;

/**
 * Function object that performs an operation on a Area.
 * @param <R> The type that the function outputs.
 */
public interface AreaVisitor<R> {

  /**
   * Visits a CuboidArea and returns a value of type R.
   * @param cuboid The cuboid area object.
   * @return A computation result of type R.
   */
  R visitCuboid(CuboidArea cuboid);

  /**
   * Visits a SphereArea and returns a value of type R.
   * @param sphere The cuboid area object.
   * @return A computation result of type R.
   */
  R visitSphere(SphereArea sphere);

  /**
   * Visits a CircleArea and returns a value of type R.
   * @param circle The cuboid area object.
   * @return A computation result of type R.
   */
  R visitCircle(CircleArea circle);


}
