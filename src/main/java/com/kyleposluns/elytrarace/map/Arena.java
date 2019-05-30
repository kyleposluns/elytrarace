package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.records.RecordBook;
import com.kyleposluns.elytrarace.records.RecordBookImpl;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Arena {

  private static final String PLAYER_DATA = "playerdata";

  private RecordBook records;

  private ArenaInfo arenaInfo;

  private Set<UUID> activePlayers;



  public Arena(ArenaInfo arenaInfo) {
    this.arenaInfo = arenaInfo;
    File arenaFile = new File(arenaInfo.getArenaId().toString());
    File playerDataFile = new File(arenaFile, PLAYER_DATA);
    playerDataFile.mkdirs();
    this.records =
            new RecordBookImpl(playerDataFile);
    this.activePlayers = new HashSet<>();
  }



  public void activate(Player player) {
    this.activePlayers.add(player.getUniqueId());

  }

  public void deactivate(Player player) {
    this.activePlayers.remove(player.getUniqueId());
  }


}
