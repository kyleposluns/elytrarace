package com.kyleposluns.elytrarace.map;

import java.util.List;

public interface ArenaManager {

  Arena getArena(String arenaName) throws IllegalArgumentException;

  List<String> getLoadedArenas();

}
