package com.github.myorama.bombermine;

import com.github.myorama.bombermine.commandexecutors.BombermineCommandExecutor;
import com.github.myorama.bombermine.listeners.BombermineBlockListener;
import com.github.myorama.bombermine.listeners.BombermineEntityListener;
import com.github.myorama.bombermine.listeners.BomberminePlayerListener;
import com.github.myorama.bombermine.models.CTFGame;
import com.github.myorama.bombermine.models.Traps;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bombermine extends JavaPlugin {

	public static final Logger log = Logger.getLogger(Bombermine.class.getName());
	private CTFGame ctfGame = null;
	private CommandExecutor cmdExec = null;
	private Traps traps = null;
	
	public Traps getTraps() {
		return traps;
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		// Register all listeners
		pm.registerEvent(Event.Type.PLAYER_INTERACT, new BomberminePlayerListener(this), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, new BomberminePlayerListener(this), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, new BombermineEntityListener(this), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, new BombermineBlockListener(this), Event.Priority.Normal, this);
				
		// Copying and merging default config.yml file to plugin folder
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		// Initialize CTF game
		this.ctfGame = new CTFGame(this);
		this.ctfGame.initialize();
		
		// Instanting CommandExecutor
		this.cmdExec = new BombermineCommandExecutor(this);
		this.getCommand("bm").setExecutor(cmdExec);

		// Initialize Traps feature
		this.traps = new Traps(new File("./plugins/Bombermine/traps.coord"));
		
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
	
	public CTFGame getCtfGame(){
		return this.ctfGame;
	}
	
	public void sendBroadcastMessage(String msg){
		Bukkit.getServer().getConsoleSender().sendMessage(msg);
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.sendMessage(ChatColor.GOLD + msg);
		}
	}
}
