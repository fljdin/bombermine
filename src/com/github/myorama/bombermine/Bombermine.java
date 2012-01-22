package com.github.myorama.bombermine;

import com.github.myorama.bombermine.listeners.BomberminePlayerListener;
import com.github.myorama.bombermine.models.CTFGame;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bombermine extends JavaPlugin {

	public static final Logger log = Logger.getLogger(Bombermine.class.getName());
	private CTFGame ctfGame = null;
	
	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE,
				new BomberminePlayerListener(this),
				Event.Priority.Normal, this);
		
		// Copying and merging default config.yml file to plugin folder
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		// Initialize CTF game
		this.ctfGame = new CTFGame(this);
		this.ctfGame.initialize();

		log.info("Bombermine plugin has been enabled.");
	}
	
	@Override
	public void onDisable() {
		log.info("Bombermine plugin has been disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
}
