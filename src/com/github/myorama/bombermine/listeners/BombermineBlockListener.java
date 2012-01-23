package com.github.myorama.bombermine.listeners;

import org.bukkit.Material;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import com.github.myorama.bombermine.Bombermine;

public class BombermineBlockListener extends BlockListener {
	public Bombermine plugin;
	
	public BombermineBlockListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		/**
		 * No drop explosion
		 */
		Bombermine.log.info(event.getType().toString()); // ça ne fait rien
		if (event.getType() == Type.ENTITY_EXPLODE) {
			event.getBlock().setType(Material.AIR);
		}
	}
}
