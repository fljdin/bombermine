package com.github.myorama.bombermine.queues;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author myorama
 *
 * @category CFT Game
 * @version 0.2
 */
public class RespawnQueue {
	private static Map<String, Long> cooldown = new HashMap<String, Long>();
	private static Long seconds = (long) 10;
	
	public static Long getSeconds() {
		return seconds;
	}

	public static void setSeconds(Long seconds) {
		RespawnQueue.seconds = seconds;
	}

	/**
	 * Start counting for player cooldown during game
	 * @param player who waiting
	 */
	public static void addCooldown(Player p) {
		cooldown.put(p.getName(), System.currentTimeMillis());
		p.sendMessage(ChatColor.BLUE + "You have to wait " + seconds + " seconds after respawn.");
	}
	
	/**
	 * Check if player is still immobilized
	 * @param player
	 * @return true or false
	 */
	public static boolean isCooldowned(Player p) {

		try {
			Long time = cooldown.get(p.getName());
			Long now = System.currentTimeMillis();
			
			if ((now - time) < seconds*1000) {
				return true;
			}
			cooldown.remove(p.getName());
			
		} catch (NullPointerException e) { }
		
		return false;
	}
}
