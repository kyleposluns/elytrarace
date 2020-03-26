package com.kyleposluns.elytrarace.arena;

import java.util.List;

public interface ArenaManager {

  Arena getArena(String arenaName) throws IllegalArgumentException;

  List<String> getLoadedArenas();

}
