package com.kyleposluns.elytrarace.arena.area;

public abstract class AbstractArea implements Area {

  protected int order;

  private AreaType type;

  public AbstractArea(AreaType type, int order) {
    this.type = type;
    this.order = order;
  }

  @Override
  public AreaType getType() {
    return this.type;
  }

  @Override
  public int compareTo(Area area) {
    return Integer.compare(this.order, area.visitArea(new GetOrdinal()));
  }

  private static class GetOrdinal implements AreaVisitor<Integer> {

    @Override
    public Integer visitCuboid(CuboidArea cuboid) {
      return cuboid.order;
    }

    @Override
    public Integer visitSphere(SphereArea sphere) {
      return sphere.order;
    }

    @Override
    public Integer visitCircle(CircleArea circle) {
      return circle.order;
    }
  }

}
