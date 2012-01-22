package com.github.myorama.bombermine;

import com.github.myorama.bombermine.commandexecutors.BombermineCommandExecutor;
import com.github.myorama.bombermine.listeners.BomberminePlayerListener;
import com.github.myorama.bombermine.models.CTFGame;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import com.github.myorama.bombermine.models.Traps;

import java.io.File;
>>>>>>> Landmines module
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bombermine extends JavaPlugin {

	public static final Logger log = Logger.getLogger(Bombermine.class.getName());
	private CTFGame ctfGame = null;
<<<<<<< HEAD
	private CommandExecutor cmdExec = null;
=======
	private Traps traps = null;
>>>>>>> Landmines module
	
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
		
<<<<<<< HEAD
		// Instanting CommandExecutor
		this.cmdExec = new BombermineCommandExecutor(this);
		this.getCommand("bm").setExecutor(cmdExec);
=======
		// Initialize Traps feature
		this.traps = new Traps(new File("./plugins/Bombermine/traps.coord"));
		this.traps.addTrap(new Location(this.getServer().getWorlds().get(0), -1, -1, -1));
		this.traps.addTrap(new Location(this.getServer().getWorlds().get(0), -1, -1, -1));
		this.traps.addTrap(new Location(this.getServer().getWorlds().get(0), -1, -2, -1));
		//this.traps.removeTrap(new Location(this.getServer().getWorlds().get(0), -1, -1, -1));
>>>>>>> Landmines module
		
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
