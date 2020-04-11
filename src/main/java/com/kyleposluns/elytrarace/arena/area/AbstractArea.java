package com.kyleposluns.elytrarace.arena.area;

public abstract class AbstractArea implements Area {

  private AreaType type;

  public AbstractArea(AreaType type) {
    this.type = type;
  }

  @Override
  public AreaType getType() {
    return this.type;
  }

}
