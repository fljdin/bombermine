package com.github.myorama.bombermine.listeners;

import java.util.ArrayList;
import java.util.List;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author myorama
 *
 * @category Game
 * @version 0.1
 */
public class DeathListener implements Listener {

	private Bombermine plugin;

	public DeathListener(Bombermine instance) {
		plugin = instance;
	}
	
	/**
	 * Add player cooldown on EntityDeathEvent and disable player drops
	 * @param event 
	 */
	@EventHandler
	public void respawnCooldown(EntityDeathEvent event){
		// TODO log killer name for scoring
		Entity e = event.getEntity();
		
		if (e instanceof Player) {
			Team t = plugin.getCtfGame().getPlayerTeam((Player)e);
			
			if (t != null) {
				// Drop only flags if presents
				List<ItemStack> flags = new ArrayList<ItemStack>();
				for (ItemStack item : event.getDrops()) {
					if (item.getType() == Material.WOOL) {
						flags.add(item);
					}
				}				
				event.getDrops().retainAll(flags);

				// Start cooldown for this player
				plugin.getCtfGame().addCooldown((Player)e);				
			}			
		}
	}
	
	/**
	 * Block player deplacements after respawn on PlayerMoveEvent
	 * @param event 
	 */
	@EventHandler
	public void respawnTime(PlayerMoveEvent event){
		if (plugin.getCtfGame().isCooldowned(event.getPlayer())) {
			Location to = event.getTo();
			Location from = event.getFrom();
			
			String currentPos = String.format("%.4f=%.4f", from.getX(), from.getZ());
			String limitPos = String.format("%.4f=%.4f", to.getX(), to.getZ());
			
			if (!limitPos.equals(currentPos)) {
				from.setYaw(to.getYaw());
				from.setPitch(to.getPitch());
				event.setTo(from.add(0,0.5,0));
			}
		}
	}
}
