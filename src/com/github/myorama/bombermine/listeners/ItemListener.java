package com.github.myorama.bombermine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;

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

	/**
	 * Flag management on PlayerPickupItemEvent
	 * 
	 * @param event
	 */
	@EventHandler
	public void pickUpFlag(PlayerPickupItemEvent event) {

		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();

		if (item.getType() == Material.WOOL) {

			Wool wool = (Wool) item.getData();
			Team team = plugin.getCtfGame().getPlayerTeam(player);

			if (team == null) {
				event.setCancelled(true);

			} else if (!team.isTeamFlag(wool)) {
				// broadcast the niouz!
				plugin.sendBroadcastMessage(String.format(
						"%s%s pickup the %s flag!", ChatColor.LIGHT_PURPLE,
						player.getDisplayName(), wool.getColor().toString()
								.toLowerCase()));

			} else {
				// TODO que faire si l'équipe récupère son propre drapeau ?
			}
		}
	}
	
	/**
	 * No flag break for owner team and spectator
	 * @param event
	 */
	@EventHandler
	public void preventFlagBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();
		MaterialData item = event.getBlock().getState().getData();

		if (item instanceof Wool) {
			Wool wool = (Wool)item;
			Team team = plugin.getCtfGame().getPlayerTeam(player);
			
			if (team == null) {
				event.setCancelled(true);
				
			} else if (team.isTeamFlag(wool)) {
				event.setCancelled(true);
			}
		}
		
	}

	@EventHandler
	public void preventFlagSwitch(PlayerItemHeldEvent event) {
		// TODO check if player doesnt delong to flag's team
	}
}
