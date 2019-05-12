package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.ElytraRace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class ArenaListener implements Listener {


  private Map<UUID, Integer> trackerId;

  private ArenaLoader arenaLoader;

  public ArenaListener(ArenaLoader arenaLoader) {
    this.arenaLoader = arenaLoader;
    this.trackerId = new HashMap<>();
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {

    Player player = event.getPlayer();
    Optional<Arena> arenaOptional = this.arenaLoader.getArena(player);

    if (arenaOptional.isPresent()) {
      Arena arena = arenaOptional.get();

      arena.lookupInfo(info -> {
        boolean outOfBounds = Arrays.stream(info.getBorders())
                .anyMatch(area -> area.isInArea(player.getLocation()));

        boolean enteredGoal =
                info.getGoal().isInArea(event.getTo()) &&
                        !info.getGoal().isInArea(event.getFrom());

        if (enteredGoal || outOfBounds) {
          arena.finish(player, enteredGoal && !outOfBounds);
          Bukkit.getScheduler().cancelTask(this.trackerId.get(player.getUniqueId()));
          this.trackerId.remove(player.getUniqueId());
          player.teleport(info.getSpawn());
          return;
        }

        boolean leftStart = info.getStart().isInArea(event.getFrom())
                && !info.getGoal().isInArea(event.getTo());

        if (leftStart) {



          arena.startRun(player);
          PlayerTracker tracker = new PlayerTracker(player, arena);
          int trackerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ElytraRace.getInstance(),
                  tracker, 20L,
                  20L);
          System.out.println("Test");
          this.trackerId.put(player.getUniqueId(), trackerId);

        }


      });


    }


  }

}
