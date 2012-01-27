package com.github.myorama.bombermine.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.github.myorama.bombermine.Bombermine;
import com.github.myorama.bombermine.models.Team;

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
		if (home.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR
				&& event.getPlayer().getLastPlayed() == 0) {
			event.getPlayer().teleport(home);
		}
	}
	
	/**
	 * Spawn dead players on PlayerRespawnEvent
	 * @param event
	 */
	@EventHandler
	public void respawnPlayer(PlayerRespawnEvent event) {
		Location home = plugin.getCtfGame().getDefaultSpawn();
		Team team = plugin.getCtfGame().getPlayerTeam(event.getPlayer());
		
		// No team ? Go home !
		if (team == null) event.getPlayer().teleport(home);
		
		// TODO detect if game is started
		event.getPlayer().teleport(team.getSpawnLoc());
	}
}
