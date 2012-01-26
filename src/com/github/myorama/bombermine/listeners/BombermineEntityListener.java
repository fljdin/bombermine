package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

@Deprecated
public class BombermineEntityListener extends EntityListener{
	private Bombermine plugin;
	
	public BombermineEntityListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		/**
		 * No block drop after explosion
		 */
		List<Block> wouldExplode = event.blockList();
		for (Block block : wouldExplode) {
			block.setType(Material.AIR);
			
			// Detect if others landmines would explode
			if (plugin.getTraps().isTrapped(block.getLocation())) {
				plugin.getTraps().explode(block.getLocation());
			}
		}
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		/**
		 * player's death rules
		 * 
		 * 1) no drop when death
		 * 2) start a cooldwon
		 * 3) TODO log killer name for scoring
		 */
		Entity e = event.getEntity();
		if (e instanceof Player) {
			event.getDrops().clear();
			plugin.getCtfGame().addCooldown((Player)e);
		}
	}
}
