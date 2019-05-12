package com.kyleposluns.elytrarace.map;

import com.kyleposluns.elytrarace.util.Clipboard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ClipboardManager implements Listener {

  private Map<UUID, Clipboard> clipboards;

  public ClipboardManager() {
    this.clipboards = new HashMap<>();
  }

  public Clipboard getClipboard(UUID player) {
    if (!this.clipboards.containsKey(player)) {
      this.clipboards.put(player, new Clipboard());
    }
    return this.clipboards.get(player);
  }

  @EventHandler
  public void onClickedBlock(PlayerInteractEvent event) {

    if (!event.getPlayer().isOp()) {
      return;
    }

    if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {

      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        getClipboard(event.getPlayer().getUniqueId()).setPos1(Objects.requireNonNull(event.getClickedBlock()).getLocation());
        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Position 1 set");
        event.setCancelled(true);
      } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        getClipboard(event.getPlayer().getUniqueId()).setPos2(Objects.requireNonNull(event.getClickedBlock()).getLocation());
        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Position 2 set");
        event.setCancelled(true);
      }
    }
  }

}
