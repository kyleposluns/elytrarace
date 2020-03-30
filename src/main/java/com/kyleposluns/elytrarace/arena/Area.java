package com.kyleposluns.elytrarace.arena;

import org.bukkit.util.Vector;

/**
 * Represents an area in space.
 */
public interface Area {

  /**
   * Determines if a 3-dimensional coordinate is in this area.
   *
   * @param vector The coordinate.
   * @return If the vector is in the space or not.
   */
  boolean contains(Vector vector);

  /**
   * A function that adds extensibility for children of this object.
   *
   * @param visitor The visitor object.
   * @param <R>     The result of the operation.
   * @return A value computed using this area.
   */
  <R> R visitArea(AreaVisitor<R> visitor);

}
