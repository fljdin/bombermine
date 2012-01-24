package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author Nittero
 */
public class BombermineEntityListener extends EntityListener{
	public Bombermine plugin;
	
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
	
}
