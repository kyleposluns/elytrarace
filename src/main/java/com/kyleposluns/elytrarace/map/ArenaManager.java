package com.kyleposluns.elytrarace.map;

import java.util.Optional;

public interface ArenaManager {

  void addArena(ArenaInfo info);

  Optional<Arena> getArena(String name);

}
