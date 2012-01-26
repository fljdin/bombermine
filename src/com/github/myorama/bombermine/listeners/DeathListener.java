package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
			event.getDrops().clear();
			plugin.getCtfGame().addCooldown((Player)e);
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
