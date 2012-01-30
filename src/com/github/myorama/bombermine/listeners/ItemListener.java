package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author myorama
 * 
 * @category Gameplay
 * @version 0.2
 */
public class ItemListener implements Listener {

	private Bombermine plugin;

	public ItemListener(Bombermine instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void throwGrenade(PlayerEggThrowEvent event) {
		// TODO catch impact location et perform explosion
	}
	
	@EventHandler
	public void useTeleportItem(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Team team = plugin.getCtfGame().getPlayerTeam(player);
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR
				&& event.getMaterial().name().contains("RECORD")) {
			player.teleport(team.getSpawnLoc());
		}
	}
}
