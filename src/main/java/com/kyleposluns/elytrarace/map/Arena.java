package com.kyleposluns.elytrarace.map;


import com.kyleposluns.elytrarace.tracking.Checkpoint;
import com.kyleposluns.elytrarace.tracking.RaceTracker;
import java.util.Queue;
import java.util.UUID;
import org.bukkit.Location;

public interface Arena {

  UUID getWorldId();

  Location getSpawn();

  RaceTracker getRaceTracker();



}
