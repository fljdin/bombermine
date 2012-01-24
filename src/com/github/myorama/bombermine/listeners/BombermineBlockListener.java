package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BombermineBlockListener extends BlockListener {
	public Bombermine plugin;
	
	public BombermineBlockListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		/**
		 * Landmine defusing
		 */
		Location loc = event.getBlock().getLocation();
		if (plugin.getTraps().isTrapped(loc)) {
			plugin.getTraps().removeTrap(loc);
			event.getPlayer().sendMessage(ChatColor.GREEN+"You have defused a trap.");
		}
	}
}
