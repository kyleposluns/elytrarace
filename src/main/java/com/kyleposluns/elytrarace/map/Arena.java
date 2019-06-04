package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.records.RaceResult;
import com.kyleposluns.elytrarace.records.RecordBook;
import org.bukkit.entity.Player;

import java.util.function.Function;

public interface Arena {

  void activate(Player player);

  void reset(Player player);

  void deactivate(Player player);

  void startRun(Player player);

  void finishRun(Player player, RaceResult result);

  double getStatistic(Function<RecordBook, Double> function);

}
