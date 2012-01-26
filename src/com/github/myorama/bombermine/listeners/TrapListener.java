package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class TrapListener implements Listener {

	private Bombermine plugin;

	public TrapListener(Bombermine instance) {
		plugin = instance;
	}
	
	/**
	 * Make trap explode on PlayerMoveEvent
	 * @param event 
	 */
	@EventHandler
	public void trapExplosion(PlayerMoveEvent event){
		Location loc = event.getTo().add(0, -0.5, 0);
		if (plugin.getTraps().isTrapped(loc)) {
			plugin.getTraps().explode(loc);
			plugin.getTraps().removeTrap(loc);
		}
	}
	
	/**
	 * No explosion drops on EntityExplodeEvent
	 * @param event 
	 */
	@EventHandler
	public  void noExplosionDrops(EntityExplodeEvent event){
		List<Block> wouldExplode = event.blockList();
		for (Block block : wouldExplode) {
			block.setType(Material.AIR);
			
			// Detect if others landmines would explode
			if (plugin.getTraps().isTrapped(block.getLocation())) {
				plugin.getTraps().explode(block.getLocation());
			}
		}
	}
	
	/**
	 * Add a trap on PlayerInteractEvent
	 * @param event 
	 */
	@EventHandler
	public void addTrap(PlayerInteractEvent event){
		if (event.getMaterial() == Material.SULPHUR && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block clickedBlock = event.getClickedBlock();
			// player can only place a trap on uncovered bloc
			if (clickedBlock.getRelative(BlockFace.UP, 1).getType() == Material.AIR) {
				// new trap location
				boolean trapped = plugin.getTraps().addTrap(clickedBlock.getLocation());
				if (trapped) {
					// remove one sulphur on hand
					int amount = event.getPlayer().getItemInHand().getAmount();
					event.getPlayer().setItemInHand(new ItemStack(Material.SULPHUR, amount - 1));
					event.getPlayer().sendMessage(ChatColor.RED + "You drop an explosive trap!");					
				}
			}
		}
	}
	
	/**
	 * Defuse trap on BlockBreakEvent
	 * @param event 
	 */
	@EventHandler
	public void defuseTrap(BlockBreakEvent event){
		Location loc = event.getBlock().getLocation();
		if (plugin.getTraps().isTrapped(loc)) {
			plugin.getTraps().removeTrap(loc);
			event.getPlayer().sendMessage(ChatColor.GREEN+"You have defused a trap.");
		}
	}
}