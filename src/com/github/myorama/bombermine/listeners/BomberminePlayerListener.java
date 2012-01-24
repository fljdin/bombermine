package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class BomberminePlayerListener extends PlayerListener {
	public Bombermine plugin;
	
	public BomberminePlayerListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		/**
		 * Trap triggered event
		 */
		Location loc = event.getTo().add(0, -1, 0);
		if (plugin.getTraps().isTrapped(loc)) {
			plugin.getTraps().explode(loc);
			plugin.getTraps().removeTrap(loc);
			event.getPlayer().sendMessage(ChatColor.RED+"You walk on an explosive trap! BOOM!");
		}
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		/**
		 * Trap drop event
		 */
		if (event.getMaterial() == Material.SULPHUR && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// new trap location
			plugin.getTraps().addTrap(event.getClickedBlock().getLocation());
			// remove one sulphur on hand
			int amount = event.getPlayer().getItemInHand().getAmount();
	        event.getPlayer().setItemInHand(new ItemStack(Material.SULPHUR.getId(), amount - 1));
			// explicite message
			event.getPlayer().sendMessage(ChatColor.RED+"You drop an explosive trap!");
		}
	}
	
	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {
		
	}
}
