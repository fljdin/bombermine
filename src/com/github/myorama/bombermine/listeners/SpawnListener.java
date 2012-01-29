package com.github.myorama.bombermine.listeners;

import com.github.myorama.bombermine.Bombermine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author myorama
 *
 * @category CTF Game
 * @version 0.2
 */
public class SpawnListener implements Listener {

	private Bombermine plugin;
	
	public SpawnListener(Bombermine instance) {
		plugin = instance;
	}
	
	/**
	 * Spawn new players on PlayerJoinEvent
	 * @param event
	 */
	@EventHandler
	public void spawnPlayerLogin(PlayerJoinEvent event) {
		Location home = plugin.getCtfGame().getDefaultSpawn();
		
		// Check if spawn is solid block and if player is new
		if (isSpawnable(home) && event.getPlayer().getLastPlayed() == 0) {
			event.getPlayer().teleport(home);
		}
	}
	
	/**
	 * Spawn dead players on PlayerRespawnEvent
	 * @param event
	 */
	@EventHandler
	public void respawnPlayer(PlayerRespawnEvent event) {
		event.setRespawnLocation(plugin.getCtfGame().respawnPlayer(event.getPlayer()));			
	}
	
	/**
	 * Check if location is on a solid block
	 * @param location
	 * @return true or false
	 */
	protected boolean isSpawnable(Location loc) {
		return loc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR;
	}
}
