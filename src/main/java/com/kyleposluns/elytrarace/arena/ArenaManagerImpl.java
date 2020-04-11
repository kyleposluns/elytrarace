package com.kyleposluns.elytrarace.arena;

import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArenaManagerImpl implements ArenaManager {

  private final Map<String, Arena> arenas;

  public ArenaManagerImpl(List<Arena> arenas) {
    this.arenas = arenas.stream().collect(Collectors.toMap(Arena::getName, arena -> arena));
  }

  @Override
  public boolean hasArena(String arenaName) {
    return this.arenas.containsKey(arenaName);
  }

  @Override
  public Arena getArena(String arenaName) throws IllegalArgumentException {
    return Optional.of(this.arenas.get(arenaName)).orElseThrow(() -> new IllegalArgumentException(
        String.format("Could not find the arena: \"%s.\"", arenaName)));
  }

  @Override
  public RaceTracker getRaceTracker(String arenaName) {

    if (!this.arenas.containsKey(arenaName)) {
      throw new IllegalArgumentException(
          String.format("An arena with the name %s doesn't exist!", arenaName));
    }

    return this.arenas.get(arenaName).getRaceTracker();
  }

  @Override
  public List<String> getLoadedArenas() {
    return new ArrayList<>(arenas.keySet());
  }
}
