package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BombermineBlockListener extends BlockListener {
	public Bombermine plugin;
	
	public BombermineBlockListener(Bombermine instance) {
		plugin = instance;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
		/*
		 * 
		 * TODO Listener à supprimer :
		 * 
		 * D'après la doc "BlockBreakEvent: Called when a block is broken by a PLAYER.
		 * EntityListener.onEntityExplode(EntityExplodeEvent event) permet de gérer l'explosion en elle même, ainsi que tout les blocs sensés exploser
		 * 
		 */
		
//		Bombermine.log.info(event.getType().toString()); // ça ne fait rien
//		if (event.getType() == Type.ENTITY_EXPLODE) {
//			event.getBlock().setType(Material.AIR);
//		}
	}
}
