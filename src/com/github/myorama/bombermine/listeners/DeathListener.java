package com.github.myorama.bombermine.listeners;

import java.util.ArrayList;
import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;
import com.github.myorama.bombermine.queues.RespawnQueue;

import org.bukkit.GameMode;

import org.bukkit.Location;
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
		Entity e = event.getEntity();
		
		if (e instanceof Player) {
			Player player = (Player)e;
			Team t = plugin.getCtfGame().getPlayerTeam(player);
			
			if (t != null) {
				// Never drop
				event.getDrops().retainAll(new ArrayList<ItemStack>());
				plugin.getCtfGame().removeRunner(player);

				// Start cooldown for this player
				if(player.getGameMode() != GameMode.CREATIVE){
					RespawnQueue.addCooldown(player);
				}
			}
		}
	}
	
	/**
	 * Block player deplacements after respawn on PlayerMoveEvent
	 * @param event 
	 */
	@EventHandler
	public void respawnTime(PlayerMoveEvent event){
		if (RespawnQueue.isCooldowned(event.getPlayer())) {
			Location to = event.getTo();
			Location from = event.getFrom();
			
			String currentPos = String.format("%.4f=%.4f", from.getX(), from.getZ());
			String limitPos = String.format("%.4f=%.4f", to.getX(), to.getZ());
			
			if (!limitPos.equals(currentPos)) {
				from.setYaw(to.getYaw());
				from.setPitch(to.getPitch());
				event.setTo(from);
				//event.setTo(from.add(0,0.5,0));
			}
		}
	}
}
