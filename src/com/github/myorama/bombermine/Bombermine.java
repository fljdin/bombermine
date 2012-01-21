package com.github.myorama.bombermine;

import java.util.logging.Logger;

import org.bukkit.command.*;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.myorama.bombermine.listeners.BomberminePlayerListener;

public class Bombermine extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		log.info("Bombermine plugin has been enabled.");
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE,
				new BomberminePlayerListener(this),
				Event.Priority.Normal, this);
	}
	
	public void onDisable() {
		log.info("Bombermine plugin has been disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
}
