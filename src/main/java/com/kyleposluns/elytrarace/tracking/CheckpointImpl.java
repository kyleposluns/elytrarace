package com.kyleposluns.elytrarace.tracking;

import com.kyleposluns.elytrarace.arena.Area;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class CheckpointImpl implements Checkpoint {

  private Set<UUID> players;

  private Area area;

  CheckpointImpl(Area area) {
    this.area = area;
    this.players = new HashSet<>();
  }

  @Override
  public boolean passed(UUID player) {
    return this.players.contains(player);
  }

  @EventHandler
  public void onEnter(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();
    if (!players.contains(playerId) && area.contains(player.getLocation().toVector())) {
      this.players.add(playerId);
    }
  }


}
