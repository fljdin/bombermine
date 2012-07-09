package com.github.myorama.bombermine.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;

/** 
 * @author myorama
 * 
 * @category CFT Game
 * @version 0.3
 */
public class FightListener implements Listener {
	private Bombermine plugin;
	
	public FightListener(Bombermine instance) {
		plugin = instance;
	}
	
	/**
	 * Cancel damages between members of team if friendly-fire is unset
	 * @param event
	 */
	@EventHandler
	public void friendlyProtection(EntityDamageByEntityEvent event) {
		boolean isFriendlyFire = plugin.getCtfGame().isFriendlyFire();
		Entity damager = event.getDamager();
		Entity victim = event.getEntity();
		
		// Check if both entities are humans
		if (damager instanceof Player && victim instanceof Player) {
			Team damagerTeam = plugin.getCtfGame().getPlayerTeam((Player)damager);
			Team victimTeam = plugin.getCtfGame().getPlayerTeam((Player)victim);
			
			// Check if both humans are same nation
			if (damagerTeam.getColor() == victimTeam.getColor()) {
				event.setCancelled(!isFriendlyFire);
			}
		}
	}
	
	@EventHandler
	public void experienceOnKill(EntityDeathEvent event) {
		// TODO give some exp on killer when victim died
		// TODO log killer name for scoring
	}
}
